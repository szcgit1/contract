package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.SysUser;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.enums.SnowflakeEnum;
import com.xtm.contract.feign.SettingServiceFeign;
import com.xtm.contract.feign.TmsFileService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.mapper.ContractTemplateCompanyMapper;
import com.xtm.contract.mapper.ContractTemplateMapper;
import com.xtm.contract.mapper.ContractTemplateMemberMapper;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.domain.ContractTemplateCompany;
import com.xtm.contract.model.domain.ContractTemplateMember;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.query.contractOther.ApplyCompanyInfo;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateCreUpdReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateInfoReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateListQryReq;
import com.xtm.contract.model.vo.FileInfo;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateDtlQryVO;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateInfoQryVO;
import com.xtm.contract.service.ContractTemplateService;
import com.xtm.contract.utils.IdWorker;
import com.xtm.contract.utils.StringUtils;
import com.xtm.file.model.vo.FileInfoVo;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/25 10:35
 * @desc
 */
@Slf4j
@Service
public class ContractTemplateServiceImpl extends ServiceImpl<ContractTemplateMapper, ContractTemplate> implements ContractTemplateService {

    @Autowired
    private ContractTemplateMapper contractTemplateMapper;

    @Autowired
    private ContractTemplateCompanyMapper contractTemplateCompanyMapper;

    @Autowired
    private ContractTemplateMemberMapper contractTemplateMemberMapper;

    @Autowired
    private TmsFileService fileService;


    @Autowired
    private TmsUserService userService;

    @Autowired
    private SettingServiceFeign settingService;

    @Autowired
    private ContractTemplateCompanyMapper templateCompanyMapper;

    /**
     * 查询合同模板列表
     *
     * @return
     * @throws Exception
     */
    @Override
    public ApiPageResult<ContractTemplateInfoQryVO> selectContractTemplateList(ContractTemplateListQryReq templateListQryReq) {
        String currentComapnyId = LoginUserContextHolder.getUser().getCompanyId();
        templateListQryReq.setCompanyId(currentComapnyId);
        Page<ContractTemplateInfoQryVO> pageParam = new Page<>(templateListQryReq.getPageNum(), templateListQryReq.getPageSize());
        IPage<ContractTemplateInfoQryVO> templateResultPage = contractTemplateMapper.getContractTemplateList(pageParam, templateListQryReq);
        if (templateResultPage == null || CollectionUtil.isEmpty(templateResultPage.getRecords())) {
            return null;
        }
        List<ContractTemplateInfoQryVO> contractTemplates = templateResultPage.getRecords();
        //查询模版下的适用公司
        List<String> templateIdList = contractTemplates.stream().distinct().filter(Objects::nonNull).map(ContractTemplateInfoQryVO::getId).collect(Collectors.toList());
        Map<String, String> templateCompanyNameMap = getTemplateApplyCompanyName(templateIdList);
        for (ContractTemplateInfoQryVO contractTemplate : contractTemplates) {
            String applyCompanyName = templateCompanyNameMap.get(contractTemplate.getId());
            if (StringUtils.isNotEmpty(applyCompanyName)){
                contractTemplate.setApplyCompanyName(applyCompanyName);
            }
        }
        ApiPageResult<ContractTemplateInfoQryVO> resultApiPageResult = ApiPageResult.<ContractTemplateInfoQryVO>builder()
                .currentPage((int)templateResultPage.getCurrent())
                .pageSize((int)templateResultPage.getSize())
                .totalPage((int)templateResultPage.getPages())
                .total((int)templateResultPage.getTotal())
                .build();

        //拿到匹配的适用单据合同模板ID
        List<String> lastestIds = getDocLastestTemplate(currentComapnyId, contractTemplates);

        //编制方企业查询
        List<String> templateIds = contractTemplates.stream().distinct().filter(x -> x != null).filter(x -> DicConstant.ENABLE_STATUS.ENABLED.equals(x.getApplicableObjectType())).map(ContractTemplateInfoQryVO::getId).collect(Collectors.toList());
        Map<String,List<ContractTemplateCompany>> templMap = getTemplateCompanyInfoById(templateIds);

        for (ContractTemplateInfoQryVO contractTemplateInfo : contractTemplates) {
            if (contractTemplateInfo != null) {
                //创建人
                UserInfoVo user = userService.getUserById(contractTemplateInfo.getCreater());
                contractTemplateInfo.setCreaterUserInfo(user);
                //合同类型描述
                contractTemplateInfo.setContractTypeDesc(settingService.getDictionaryById(contractTemplateInfo.getContractType().longValue()).getName());
                //单据类型描述
                if (contractTemplateInfo.getContractDocumentType() != null) {
                    contractTemplateInfo.setDocumentTypeDesc(settingService.getDictionaryById(contractTemplateInfo.getContractDocumentType().longValue()).getName());
                }

                if(DicConstant.ENABLE_STATUS.ENABLED.equals(contractTemplateInfo.getApplicableObjectType()) && CollUtil.isNotEmpty(templMap)){
                    contractTemplateInfo.setApplyCompanys(templMap.get(contractTemplateInfo.getId()));
                }

                //框架合同和最新的两条单据合同显示绿点
                if (lastestIds.contains(contractTemplateInfo.getId()) || DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT.equals(contractTemplateInfo.getContractType())) {
                    contractTemplateInfo.setUserFlag(true);
                }
                //业务性质描述
                if (contractTemplateInfo.getBusinessType() != null) {
                    contractTemplateInfo.setBusinessTypeDesc(settingService.getDictionaryById(contractTemplateInfo.getBusinessType().longValue()).getName());
                }
            }
        }
        resultApiPageResult.setList(contractTemplates);
        return resultApiPageResult;
    }

    /**
     * 获取模版下的适用公司
     * @param templateIds
     * @return
     */
    private Map<String,String> getTemplateApplyCompanyName(List<String> templateIds) {
        LambdaQueryWrapper<ContractTemplateCompany> queryTemplateCompanyWrapper = Wrappers.<ContractTemplateCompany>lambdaQuery()
                .in(ContractTemplateCompany::getContractTemplateId, templateIds);
        List<ContractTemplateCompany> templateCompanyList = templateCompanyMapper.selectList(queryTemplateCompanyWrapper);
        //按照模版id分组
        Map<String,ContractTemplateCompany> templateCompanyMap =  new HashMap<>();
        for (ContractTemplateCompany contractTemplateCompany : templateCompanyList) {
            templateCompanyMap.put(contractTemplateCompany.getContractTemplateId(), contractTemplateCompany);
        }
        //根据模版id分组
        Map<String,Integer> templateCompanyCountMap = new LinkedHashMap<>();
        Map<String,String> templateCompanyNameMap = new LinkedHashMap<>();
        for (ContractTemplateCompany contractTemplateCompany : templateCompanyList) {
            String contractTemplateId = contractTemplateCompany.getContractTemplateId();
            if (templateCompanyCountMap.containsKey(contractTemplateId)) {
                templateCompanyCountMap.put(contractTemplateId, templateCompanyCountMap.get(contractTemplateId) + 1);
            } else {
                templateCompanyCountMap.put(contractTemplateId, 1);
                templateCompanyNameMap.put(contractTemplateId, contractTemplateCompany.getCompanyName());
            }
        }
        for (Map.Entry<String, String> entry : templateCompanyNameMap.entrySet()) {
            String templateId = entry.getKey();
            Integer count = templateCompanyCountMap.get(templateId);
            if(count>1){
                String templateName = entry.getValue();
                templateCompanyNameMap.put(templateId, templateName+"等【"+count+"】");
            }
        }
        return templateCompanyNameMap;
    }

    private List<String> getDocLastestTemplate(String companyId, List<ContractTemplateInfoQryVO> contractTemplates) {
        if (contractTemplates == null) {
            throw new BusinessException(-1, "平台默认合同模板未配置");
        }

        List<String> lastestIds = new ArrayList<>();
        boolean orderUseFlag = false;
        boolean batchUseFlag = false;
        boolean tripartiteOrderUseFlag = false;
        List<ContractTemplate> orderTemplateInfoQry = contractTemplateMapper.getLastestDirectTemplateByCompanyId(Collections.singletonList(companyId),
                DicConstant.DOCUMENT_TYPE.ORDER, DicConstant.CONTRACT_BUSINESS_TYPE.BOTH);
        if (CollUtil.isNotEmpty(orderTemplateInfoQry)) {
            lastestIds.add(orderTemplateInfoQry.get(0).getId());
            orderUseFlag = true;
        }
        List<ContractTemplate> batchTemplateInfoQry = contractTemplateMapper.getLastestDirectTemplateByCompanyId(Collections.singletonList(companyId),
                DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH, DicConstant.CONTRACT_BUSINESS_TYPE.BOTH);
        if (CollUtil.isNotEmpty(batchTemplateInfoQry)) {
            lastestIds.add(batchTemplateInfoQry.get(0).getId());
            batchUseFlag = true;
        }
        //三方订单合同
        List<ContractTemplate> tripartiteOrderTemplateInfoQry = contractTemplateMapper.getLastestDirectTemplateByCompanyId(Collections.singletonList(companyId),
                DicConstant.DOCUMENT_TYPE.ORDER, DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE);
        if (CollUtil.isNotEmpty(tripartiteOrderTemplateInfoQry)) {
            lastestIds.add(tripartiteOrderTemplateInfoQry.get(0).getId());
            tripartiteOrderUseFlag = true;
        }

        if (CollUtil.isNotEmpty(contractTemplates)) {
            for (ContractTemplateInfoQryVO ctmInfo : contractTemplates) {
                if (DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.equals(ctmInfo.getContractType())) {
                    if (DicConstant.DOCUMENT_TYPE.ORDER.equals(ctmInfo.getContractDocumentType())) {
                        if (!orderUseFlag && DicConstant.CONTRACT_BUSINESS_TYPE.BOTH.equals(ctmInfo.getBusinessType())) {
                            lastestIds.add(ctmInfo.getId());
                            orderUseFlag = true;
                        }
                        if (!tripartiteOrderUseFlag && DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE.equals(ctmInfo.getBusinessType())) {
                            lastestIds.add(ctmInfo.getId());
                            tripartiteOrderUseFlag = true;
                        }
                    } else if (DicConstant.DOCUMENT_TYPE.DISPATCH_BATCH.equals(ctmInfo.getContractDocumentType())) {
                        if (!batchUseFlag) {
                            lastestIds.add(ctmInfo.getId());
                            batchUseFlag = true;
                        }
                    }
                }
            }
        }
        return lastestIds;
    }

    /**
     * 查询合同模板
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ContractTemplateDtlQryVO selectContractTempateById(String id) {
        if (StrUtil.isBlank(id)) {
            log.error("参数不能为空！");
            return null;
        }
        ContractTemplateDtlQryVO contractTemplateDtl = new ContractTemplateDtlQryVO();
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(id);
        if (contractTemplate != null) {
            BeanUtils.copyProperties(contractTemplate, contractTemplateDtl);
            contractTemplateDtl.setIsDefault(contractTemplate.getIsDefault() != null ? 1 : 0);
            //签署照片
            if (StrUtil.isNotBlank(contractTemplate.getSignPhotoId())) {
                FileInfoVo file = fileService.getFileById(contractTemplate.getSignPhotoId());
                if (file != null) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileId(file.getId());
                    fileInfo.setFileUrl(file.getFileServerUrl() + file.getUrl());
                    fileInfo.setFileDesc(file.getName());
                    contractTemplateDtl.setSignPhotoInfo(fileInfo);
                }
            }
        }
        //查询适用公司
        List<ApplyCompanyInfo> companyList = selectApplyCompanys(id);
        contractTemplateDtl.setApplyCompanyList(companyList);
        //查询适用会员
        List<Long> memberTypeList = selectApplyMembers(id);
        contractTemplateDtl.setApplyMemberList(memberTypeList);
        return contractTemplateDtl;
    }

    @Override
    public List<ContractTemplate> selectContractTempateByConType(Integer contractType) throws Exception {
        //默认
        List<ContractTemplate> defaultTemplates = getDefaultContractTemplate(contractType);
        //自定义
        List<ContractTemplate> definedTemplates = getDefinedContractTemplate(contractType, null);

        if (CollUtil.isNotEmpty(definedTemplates)) {
            defaultTemplates.addAll(definedTemplates);
        }
        return defaultTemplates;
    }

    @Override
    public ContractTemplate selectContractTempateByCompany(Integer documentType, String companyId, Integer businessType) {
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        if (StrUtil.isBlank(companyId) || documentType == null) {
            return null;
        }
        //1.优先取定向给这个公司的单据模板
        List<ContractTemplate> contractDicTemplates = contractTemplateMapper.getLastestDirectTemplateByCompanyId(Collections.singletonList(companyId),
                documentType, businessType);
        // companyId不是一级公司且查询不到合同模版，那么使用一级公司查询合同模版
        if(!companyId.equals(sessionInfo.getPlatformCompanyId()) && CollUtil.isEmpty(contractDicTemplates)){
            contractDicTemplates = contractTemplateMapper.getLastestDirectTemplateByCompanyId(Collections.singletonList(sessionInfo.getPlatformCompanyId()),
                    documentType, businessType);
        }
        if (CollUtil.isNotEmpty(contractDicTemplates)) {
            return contractDicTemplates.get(0);
        }
        //2.取自定义通用且最新的模板
        ContractTemplate contractTemplate = new ContractTemplate();
        contractTemplate.setContractType(DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT);
        contractTemplate.setContractDocumentType(documentType);
        contractTemplate.setEnabledStatus(DicConstant.ENABLE_STATUS.ENABLED);
        contractTemplate.setApplicableObjectType(0);
        contractTemplate.setCompanyId(companyId);
        contractTemplate.setIsDelete(DicConstant.IS_DELETE.NO);
        contractTemplate.setBusinessType(businessType);
        log.info("查询创建合同的对应模板入参：" + JSON.toJSONString(contractTemplate));
        List<ContractTemplate> contractTemplates = contractTemplateMapper.selectList(new QueryWrapper<>(contractTemplate).orderByDesc("create_time"));
        // companyId不是一级公司且查询不到合同模版，那么使用一级公司查询合同模版
        if (!companyId.equals(sessionInfo.getPlatformCompanyId()) && ObjectUtil.isEmpty(contractTemplates)) {
            contractTemplate.setCompanyId(sessionInfo.getPlatformCompanyId());
            contractTemplates = contractTemplateMapper.selectList(new QueryWrapper<>(contractTemplate).orderByDesc("create_time"));
        }
        //3.取默认模板
        if (contractTemplates == null || contractTemplates.isEmpty()) {
            //未查到自定义的就查默认合同模板
            contractTemplates = this.getDefaultContractTemplate(DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT);
            for (ContractTemplate defContractTemplate : contractTemplates) {
                //非明细合同或者明细合同的单据类型不一致时跳出
                if (defContractTemplate.getContractDocumentType() == null) {
                    continue;
                }
                if (!defContractTemplate.getContractDocumentType().equals(documentType)) {
                    continue;
                }
                //业务性质不一致时跳出
                if (!defContractTemplate.getBusinessType().equals(businessType)) {
                    continue;
                }
                return defContractTemplate;
            }
            if (Objects.equals(businessType, DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
                return null;
            }
        }
        return contractTemplates.get(0);
    }

    /**
     * 创建合同模板
     *
     * @param inParam
     * @throws Exception
     */
    @Override
    public String creatContractTemplate(ContractTemplateCreUpdReq inParam) throws Exception {
        boolean flag = checkContractTemplateParam(inParam);
        if (flag == false) {
            log.error("参数不能为空！");
            throw new BusinessException(ResultCode.VALIDATOR.getCode(),"");
        }
        String templateId = inParam.getTemplateID();
        SysUser session = LoginUserContextHolder.getUser();
        ContractTemplate contractTemplate = new ContractTemplate();
        BeanUtils.copyProperties(inParam, contractTemplate);
        contractTemplate.setIsDelete(DicConstant.IS_DELETE.NO);
        contractTemplate.setIsDefault(Boolean.FALSE);
        if(DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT.equals(inParam.getContractType())) {
            contractTemplate.setApplicableObjectType(DicConstant.ENABLE_STATUS.ENABLED);
        }
        Integer businessType = inParam.getBusinessType();
        if (businessType == null) {
            businessType = DicConstant.CONTRACT_BUSINESS_TYPE.BOTH;
        }
        contractTemplate.setBusinessType(businessType);
        if (StrUtil.isBlank(templateId)) {
            templateId = IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT);
            contractTemplate.setId(templateId);
            contractTemplate.setCompanyId(session.getCompanyId());
            contractTemplate.setCreater(session.getId());
            contractTemplate.setCreateTime(new Date());
            contractTemplate.setEnabledStatus(DicConstant.ENABLE_STATUS.ENABLED);
            Integer count = this.lambdaQuery().eq(ContractTemplate::getBusinessType, businessType)
                    .eq(ContractTemplate::getContractDocumentType, inParam.getContractDocumentType())
                    .eq(ContractTemplate::getContractType, inParam.getContractType())
                    .eq(ContractTemplate::getIsDelete, DicConstant.IS_DELETE.NO)
                    .eq(ContractTemplate::getIsDefault, true)
                    .count().intValue();
            //第一个保存的合同模板设置为默认模板
            if (count == 0) {
                contractTemplate.setIsDefault(true);
            }
            //新增
            contractTemplateMapper.insert(contractTemplate);
        } else {
            ContractTemplate hisTemplate = contractTemplateMapper.selectById(inParam.getTemplateID());
            if (hisTemplate == null) {
                throw new Exception("当前要修改的合同模板不存在！");
            }
            contractTemplate.setId(inParam.getTemplateID());
            contractTemplate.setModifier(session.getId());
            contractTemplate.setModifyTime(new Date());
            contractTemplate.setIsDefault(hisTemplate.getIsDefault());
            //修改
            contractTemplateMapper.updateById(contractTemplate);
            //删除历史适用范围
            deleteHistoryRange(templateId);
        }
        //保存适用公司
        saveApplyCompanys(inParam.getApplyCompanyInfos(), templateId);
        //保存适用会员
        saveApplyMembers(inParam.getMemberTypes(), templateId);
        return templateId;
    }

    @Override
    public void deleteContractTemplate(ContractTemplateInfoReq inParam) throws BusinessException {
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(inParam.getTemplateId());
        if (contractTemplate == null) {
            log.info("当前要删除的合同模板不存在！");
            Integer code = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(), FunctionCode.CON_THIRD.getCode(), ContractErrorCode.TEMPLATE_NOT_EXITS.getCode());
            String msg = ContractErrorCode.TEMPLATE_NOT_EXITS.getMessage();
            throw new BusinessException(code, msg);
        }
        contractTemplate.setIsDelete(DicConstant.IS_DELETE.YES);
        contractTemplateMapper.updateById(contractTemplate);
    }

    @Override
    public void enabledStatuChange(ContractTemplateInfoReq inParam) throws Exception {
        ContractTemplate contractTemplate = contractTemplateMapper.selectById(inParam.getTemplateId());
        if (contractTemplate == null) {
            log.info("当前要改变状态的合同模板不存在！");
            throw new Exception("当前要改变状态的合同模板不存在！");
        }
        contractTemplate.setEnabledStatus(inParam.getEnabledStatus() == null ? DicConstant.ENABLE_STATUS.ENABLED : inParam.getEnabledStatus());
        contractTemplateMapper.updateById(contractTemplate);
    }

    /**
     * 保存适用公司
     *
     * @param companyInfoList
     * @param id
     */
    private void saveApplyCompanys(List<ApplyCompanyInfo> companyInfoList, String id) {
        if (!CollUtil.isEmpty(companyInfoList)) {
            for (ApplyCompanyInfo companyInfo : companyInfoList) {
                ContractTemplateCompany contractTemplateCompany = new ContractTemplateCompany();
                contractTemplateCompany.setId(IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT));
                contractTemplateCompany.setContractTemplateId(id);
                contractTemplateCompany.setCompanyId(companyInfo.getCompanyId());
                contractTemplateCompany.setCompanyName(companyInfo.getCompanyName());
                contractTemplateCompany.setCreater(LoginUserContextHolder.getUser().getId());
                contractTemplateCompany.setCreateTime(new Date());
                contractTemplateCompanyMapper.insert(contractTemplateCompany);
            }
        }
    }

    /**
     * 保存适用会员
     *
     * @param memberTypes
     * @param id
     */
    private void saveApplyMembers(List<Integer> memberTypes, String id) {
        if (!CollUtil.isEmpty(memberTypes)) {
            for (Integer memberType : memberTypes) {
                ContractTemplateMember contractTemplateMember = new ContractTemplateMember();
                contractTemplateMember.setId(IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT));
                contractTemplateMember.setContractTemplateId(id);
                contractTemplateMember.setCompanyMemberType(memberType);
                contractTemplateMember.setCreater(LoginUserContextHolder.getUser().getId());
                contractTemplateMember.setCreateTime(new Date());
                contractTemplateMemberMapper.insert(contractTemplateMember);
            }
        }
    }

    /**
     * 删除模板历史适用范围
     *
     * @param id
     */
    private void deleteHistoryRange(String id) {
        if (StrUtil.isBlank(id)) {
            log.info("删除历史适用范围的模板ID不能为空");
            return;
        }
        Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("contract_template_id", id);
        //删除适用公司
        contractTemplateCompanyMapper.deleteByMap(deleteMap);
        //删除适用会员
        contractTemplateMemberMapper.deleteByMap(deleteMap);
    }

    /**
     * 查询适用公司
     *
     * @param id
     * @return
     */
    private List<ApplyCompanyInfo> selectApplyCompanys(String id) {
        QueryWrapper<ContractTemplateCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("company_id,company_name").eq("contract_template_id", id);
        List<ContractTemplateCompany> templateCompanies = contractTemplateCompanyMapper.selectList(queryWrapper);
        List<ApplyCompanyInfo> applyCompanyInfos = new ArrayList<>();
        for (ContractTemplateCompany company : templateCompanies) {
            if (company != null) {
                ApplyCompanyInfo applyCompanyInfo = new ApplyCompanyInfo();
                applyCompanyInfo.setCompanyId(company.getCompanyId());
                applyCompanyInfo.setCompanyName(company.getCompanyName());
                applyCompanyInfos.add(applyCompanyInfo);
            }
        }
        return applyCompanyInfos;
    }

    /**
     * 查询适用会员
     *
     * @param id
     * @return
     */
    private List<Long> selectApplyMembers(String id) {
        QueryWrapper<ContractTemplateMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("company_member_type").eq("contract_template_id", id);
        List<ContractTemplateMember> templateMembers = contractTemplateMemberMapper.selectList(queryWrapper);
        List memberTypes = new ArrayList();
        for (ContractTemplateMember contractTemplateMember : templateMembers) {
            memberTypes.add(contractTemplateMember.getCompanyMemberType());
        }
        return memberTypes;
    }

    /**
     * 查询默认模板
     *
     * @return
     */
    public List<ContractTemplate> getDefaultContractTemplate(Integer contractType) {
        log.info("查询类型为{}的默认模板", contractType);
        List<ContractTemplate> contractTemplateList = new LambdaQueryChainWrapper<>(contractTemplateMapper)
                .eq(ContractTemplate::getIsDefault, Boolean.TRUE)
                .eq(ContractTemplate::getContractType, contractType)
                .list();
        log.info("查询默认合同模板数量为：{}", CollUtil.isEmpty(contractTemplateList) ? null : contractTemplateList.size());
        return contractTemplateList;
    }

    /**
     * 查询自定义(非默认)模板
     *
     * @return
     */
    public List<ContractTemplate> getDefinedContractTemplate(Integer contractType, String companyId) {
        log.info("查询类型为{}的自定义模板", contractType);
        ContractTemplate contractTemplate = new ContractTemplate();
        contractTemplate.setIsDefault(Boolean.FALSE);
        contractTemplate.setIsDelete(DicConstant.IS_DELETE.NO);
        contractTemplate.setContractType(contractType);
        contractTemplate.setCompanyId(StrUtil.isBlank(companyId) ? LoginUserContextHolder.getUser().getCompanyId() : companyId);
        List<ContractTemplate> contractTemplateList = contractTemplateMapper.selectList(new QueryWrapper<>(contractTemplate).orderByDesc("create_time").last("limit 20"));
        log.info("查询自定义合同模板数量为：{}", CollUtil.isEmpty(contractTemplateList) ? null : contractTemplateList.size());
        return contractTemplateList;
    }

    private Boolean checkContractTemplateParam(ContractTemplateCreUpdReq inParam) {
        if (inParam == null) {
            return false;
        }
        if (StrUtil.isBlank(inParam.getTitle())) {
            log.info("模板标题不能为空！");
            return false;
        }
        if (StrUtil.isBlank(inParam.getContent())) {
            log.info("模板内容不能为空！");
            return false;
        }
        if (null == inParam.getContractType()) {
            log.info("模板合同类型不能为空！");
            return false;
        }
        if (DicConstant.CONTRACT_TYPE.DETAILED_CONTRACT.equals(inParam.getContractType()) && null == inParam.getContractDocumentType()) {
            log.info("模板单据类型不能为空！");
            return false;
        }
        if (StrUtil.isBlank(inParam.getTemplateName())) {
            log.info("模板名称不能为空！");
            return false;
        }
        return true;
    }

    private Map<String,List<ContractTemplateCompany>> getTemplateCompanyInfoById(List<String> templateIds) {
        if(CollUtil.isEmpty(templateIds)){
            return null;
        }
        LambdaQueryWrapper<ContractTemplateCompany> queryTemplateCompanyWrapper = Wrappers.<ContractTemplateCompany>lambdaQuery()
                .in(ContractTemplateCompany::getContractTemplateId, templateIds);
        Map<String,List<ContractTemplateCompany>> templateCompanyMap = new HashMap<>();
        List<ContractTemplateCompany> templateCompanyList = templateCompanyMapper.selectList(queryTemplateCompanyWrapper);
        if (CollUtil.isNotEmpty(templateCompanyList)) {
            // 进行拆解封装
            templateCompanyMap = templateCompanyList.stream().collect(Collectors.groupingBy(ContractTemplateCompany::getContractTemplateId));
        }
        return templateCompanyMap;
    }
}
