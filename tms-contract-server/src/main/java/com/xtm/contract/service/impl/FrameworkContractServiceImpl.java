package com.xtm.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.enums.DicConstant;
import com.xtm.common.model.SysUser;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.ServerCode;
import com.xtm.contract.enums.SnowflakeEnum;
import com.xtm.contract.feign.TmsCompanyService;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.contract.utils.IdWorker;

import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.mapper.ContractAttachMapper;
import com.xtm.contract.mapper.ContractMapper;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.domain.ContractAttach;
import com.xtm.contract.model.domain.ContractMigrate;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.query.contract.FrameContractCreUpdReq;
import com.xtm.contract.model.param.FrameContractPartnerReq;
import com.xtm.contract.model.vo.FileInfo;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.contract.FrameContractDtlQryVO;
import com.xtm.contract.model.vo.PartnerFraContractVO;
import com.xtm.contract.model.vo.contractOther.CompanyVasInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.contract.service.ContractEqbSignService;
import com.xtm.contract.service.ContractMigrateService;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.FrameworkContractService;
import com.xtm.contract.utils.ContractSessionUtil;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.FileHelper;
import com.xtm.contract.utils.OrganizationOrSettingHelper;
import com.xtm.contract.utils.PdfHelper;
import com.xtm.contract.feign.TmsFileService;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.file.model.vo.FileInfoVo;
import com.xtm.user.model.vo.ContactVo;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/25 15:54
 * @desc 框架合同业务SERVICE
 */
@Slf4j
@Service
public class FrameworkContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements FrameworkContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private TmsCompanyService companyService;

    @Autowired
    private TmsUserService userService;

    @Autowired
    private TmsFileService fileService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractEqbSignService eqbSignService;

    @Autowired
    private OrganizationOrSettingHelper organizationOrSettingHelper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private EqbHelper eqbHelper;

    @Autowired
    private ContractAttachMapper attachMapper;
    @Autowired
    private ContractMigrateService contractMigrateService;


    @Override
    public void creatOrUpdFrameContract(FrameContractCreUpdReq inParam) throws Exception {
        //校验入参
        boolean flag = checkFrameContractCreParam(inParam);
        ContractResult result = new ContractResult();
        if (flag == false) {
            log.error("创建框架合同参数错误！");
            throw new BusinessException(ResultCode.VALIDATOR.getCode(),"");
        }
        // 是否是新合同
        boolean isNew = true;
        Contract contract = new Contract();
        //当前session
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        String currentUserId = ContractSessionUtil.getCurrentUserID(sessionInfo);
        String currentCompanyId = ContractSessionUtil.getCurrentCompanyID(sessionInfo);

        if (StrUtil.isNotBlank(inParam.getContractID())) {
            contract = contractMapper.selectById(inParam.getContractID());
            if (contract != null) {
                isNew = false;
                inParam.setContractID(contract.getId());
            }
        }else {
            //拷贝字段
            BeanUtil.copyProperties(inParam, contract);
            contract.setValidStartDate(inParam.getStartDate());
            contract.setValidEndDate(inParam.getEndDate());
            contract.setTradeTime(contract.getContractDate()); // 兼容框架合同查询条件
            contract.setIsDelete(DicConstant.IS_DELETE.NO);
            contract.setContractTemplateId(inParam.getContractTemplateID());
            contract.setContractTemplateName(inParam.getTemplateName());//合同模板名称
            contract.setTrustorCompanyName(companyService.findCompanyById(inParam.getTrustorCompanyId()).getName());//托运人名称
            contract.setCarrierCompanyName(companyService.findCompanyById(inParam.getCarrierCompanyId()).getName());//承运人公司名称
            if (Objects.equals(contract.getContractTemplateBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)) {
                contract.setAgentCompanyName(companyService.findCompanyById(inParam.getAgentCompanyId()).getName());// 代办人公司名称
                contract.setAgentSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);

            }
        }

        //新增
        if (isNew) {
            String id = IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT);
            inParam.setContractID(id);
            contract.setId(id);
            contract.setDocumentId(IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT));
            contract.setCompileSideId(currentCompanyId);
            contract.setTrustorSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            contract.setCarrierSignStatus(DicConstant.CONTRACT_SIGN_STATUS.WAIT);
            contract.setCreater(currentUserId);
            contract.setCreateTime(LocalDateTime.now());
            contract.setVer(1);
            contractMapper.insert(contract);
        } else {
            contract.setModifier(currentUserId);
            contract.setModifyTime(LocalDateTime.now());
            contract.setCreateTime(null);
            contractMapper.updateById(contract);
        }
        //添加合同附件
        addContractAttach(inParam.getContractID(), inParam.getFileIds(), sessionInfo);
//        try {
//            //电子签章
//            result = createEcFrameContractPdf(contract.getId(), sessionInfo);
//            if (result != null && result.getCode() != ContractResult.XIAONIU_CONTRACT_BACK_0.getCode()) {
//                log.info("创建框架合同时的错误信息为：" + JSON.toJSONString(result));
//                //错误信息落库
//                contractService.saveErrorInfoToContract(result, inParam.getContractID());
//            }
//        }catch (BusinessException e){
//            log.error("框架合同电子签章生成失败" + e.getMessage());
//            result.setCode(e.getCode());
//            result.setMsg(e.getMessage());
//            contractService.saveErrorInfoToContract(result, inParam.getContractID());
//        }catch (Exception ex){
//            log.error("框架合同电子签章生成失败" + ex);
//            result.setCode(ContractResult.XIAONIU_CONTRACT_BACK_0.getCode());
//            result.setMsg(ex.getMessage());
//            contractService.saveErrorInfoToContract(result,inParam.getContractID());
//        }
    }


    @Override
    public ContractResult createEcFrameContractPdf(String contractId, SysUser sessionInfo) {
        ContractResult result = new ContractResult();

        FrameContractDtlQryVO contractInfo = getFrameContractDtlById(contractId);
        //生成本地合同
        result = createLocalFramePdf(contractInfo);
        //电子签章
        CompanyVasInfo vasInfo = eqbHelper.vasAuth();
        //校验订阅服务授权信息
        result = eqbHelper.checkVasInfo(vasInfo);
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() != result.getCode()) {
            return result;
        }
        //获取e签宝配置
        EqbConfigInfo eqbConfigInfo = eqbHelper.getEqbConfigInfo();

        EContractEcSignReq ecSignReq = new EContractEcSignReq();
        BeanUtils.copyProperties(contractInfo,ecSignReq);
        ecSignReq.setContractId(contractInfo.getId());
        ecSignReq.setLocalPdfUrl(contractInfo.getEcContractPathUrl());
        //生成电子合同
        contractService.signEqbElectronicSeal(eqbConfigInfo, ecSignReq,vasInfo);
        return  result;
    }

    @Override
    public FrameContractDtlQryVO getFrameContractDtlById(String contractId) {
        log.info("当前合同ID:"+contractId);
        FrameContractDtlQryVO frameConOut = new FrameContractDtlQryVO();
        Contract contract = null;
        ContractMigrate contractMigrate = contractMigrateService.getById(contractId);
        if (contractMigrate == null) {
            contract = contractMapper.selectById(contractId);
        } else {
            contract = contractMapper.getMigrateContract(contractMigrate.getId(),contractMigrate.getTableSuffix());
        }
        if (contract == null) {
            log.info("当前框架合同信息为空！");
            return  null;
        }

        // copy字段
        BeanUtils.copyProperties(contract, frameConOut);
        // 设置托运人公司信息
        if (StrUtil.isNotBlank(contract.getTrustorCompanyId())) {
            CompanyBO trustorInfo = companyService.getCompanyById(contract.getTrustorCompanyId());
            ContactVo trustContact = contractService.selectContractSideAdminInfo(trustorInfo);//托运方联系人
            trustorInfo.setContact(trustContact);
            frameConOut.setTrustorCompany(trustorInfo);
        }

        // 设置承运人公司信息
        if (StrUtil.isNotBlank(contract.getCarrierCompanyId())) {
            CompanyBO carrierInfo = companyService.getCompanyById(contract.getCarrierCompanyId());
            ContactVo carrierContact = contractService.selectContractSideAdminInfo(carrierInfo);//承运方联系人
            carrierInfo.setContact(carrierContact);
            frameConOut.setCarryCompany(carrierInfo);
        }

//        //  todo 当合同为三方
//        if (StrUtil.isNotBlank(contract.getAgentCompanyId()) && Objects.equals(contract.getContractTemplateBusinessType(), DicConstant.CONTRACT_BUSINESS_TYPE.TRIPARTITE)  ){
//
//            CompanyBO carrierInfo = companyService.getCompanyById(contract.getAgentCompanyId());
//            Contact carrierContact = contractService.selectContractSideAdminInfo(carrierInfo);//承运方联系人
//            carrierInfo.setContact(carrierContact);
//            frameConOut.setCarryCompany(carrierInfo);
//
//        }
//
//
//

        // 设置编制方公司信息
        if (StrUtil.isNotBlank(contract.getCompileSideId())) {
            CompanyVo compileSideInfo = companyService.findCompanyById(contract.getCompileSideId());
            frameConOut.setCompileSideCompany(compileSideInfo);
        }

        //创建人信息
        if (StrUtil.isNotBlank(contract.getCreater())) {
            UserInfoVo userInfo = userService.getUserById(contract.getCreater());
            frameConOut.setUserInfo(userInfo);
        }
        //本地pdf
        if(StrUtil.isNotBlank(contract.getEcContractPath())){
            FileInfoVo file = fileService.getFileById(contract.getEcContractPath());
            if(file != null){
                frameConOut.setEcContractPathUrl(file.getFileServerUrl() + file.getUrl());
            }
        }

        //电子签章pdf
        if(StrUtil.isNotBlank(contract.getEcContractPdfId())){
            FileInfoVo file = fileService.getFileById(contract.getEcContractPdfId());
            if(file != null){
                frameConOut.setEcContractPdfUrl(file.getFileServerUrl() + file.getUrl());
            }
        }

        //附件信息
        List<ContractAttach> contractAttachList = new ArrayList<>();
        if (contractMigrate == null) {
            contractAttachList = getContractAttachInfoByContractIds(Arrays.asList(contractId));
        } else {
            contractAttachList = attachMapper.getMigrateContractAttach(contractMigrate.getId(),contractMigrate.getTableSuffix());
        }
        List<FileInfo> fileInfoList = getContractFileInfos(contractAttachList);
        frameConOut.setAttachFileInfo(fileInfoList);

        SysUser sessionInfo = LoginUserContextHolder.getUser();
        //是否授权
        Map<String, String> buttonResult = new HashMap<>();
        buttonResult.put("btnEcSigned", "hide");
        buttonResult.put("btnUpdate", "hide");
        buttonResult.put("btnDelete", "hide");
        try {
            CompanyVasInfo vasInfo = eqbHelper.vasAuth();
            if (vasInfo != null) {
                if (vasInfo.getAuthorizedStatus() && vasInfo.getEnabledStatus()) {
                    buttonResult.put("btnEcSigned", "show");
                }
            }
        }catch (Exception exception){
            log.error("订阅服务调用异常:" + exception.getMessage());
        }
        //删除、修改权限
        if(sessionInfo.getCompanyId().equals(contract.getCompileSideId())){
            buttonResult.put("btnUpdate", "show");
            buttonResult.put("btnDelete", "show");
        }
        frameConOut.setButtonPermission(buttonResult);
        return frameConOut;
    }

    /**
     * 根据合同ID获取合同附件
     *
     * @param contractIds
     * @return
     */
    public List<ContractAttach> getContractAttachInfoByContractIds(List<String> contractIds) {
        QueryWrapper<ContractAttach> qw = new QueryWrapper<>();
        qw.in("contract_id", contractIds);
        List<ContractAttach> contractAttachList = attachMapper.selectList(qw);
        return contractAttachList;
    }

    /**
     * 根据合同附件获取合同文件
     *
     * @param contractAttaches
     * @return
     */
    public List<FileInfo> getContractFileInfos(List<ContractAttach> contractAttaches) {
        if(CollUtil.isEmpty(contractAttaches)){
            return Arrays.asList();
        }
        List<FileInfo> fileInfos = new ArrayList<>();
        //获取文件URL
        List<String> ids = contractAttaches.stream().filter(e->StrUtil.isNotBlank(e.getFileId())).map(ContractAttach::getFileId).collect(Collectors.toList());
        List<FileInfoVo> files = fileService.listByIds(ids);
        for (FileInfoVo file : files) {
            if(file != null){
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileId(file.getId());
                fileInfo.setFileUrl(file.getFileServerUrl() + file.getUrl());
                fileInfo.setFileDesc(file.getName());

                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }


    @Override
    public ContractPathVO rebuildFraEcContract(String contractId) {
        ContractPathVO contractPath = new ContractPathVO();
        SysUser sessionInfo = LoginUserContextHolder.getUser();

        FrameContractDtlQryVO contractInfo = getFrameContractDtlById(contractId);
        CompanyVasInfo vasInfo = eqbHelper.vasAuth();
        //校验订阅服务授权信息
        ContractResult result = eqbHelper.checkVasInfo(vasInfo);
        log.info("增值服务授权信息结果:"+JSON.toJSONString(result));
        if (ContractResult.XIAONIU_CONTRACT_BACK_0.getCode() != (result.getCode())) {
            contractPath.setResultCode(result.getCode());
            contractPath.setResultDesc(result.getMsg());
            log.info("增值服务授权查询返回");
            return contractPath;
        }
        try {
            //获取E签宝的配置信息
            EqbConfigInfo configInfo = eqbHelper.getEqbConfigInfo();
            //流程文档Id不存在需要重新生成签章
            if (StrUtil.isBlank(contractInfo.getEcContractEsignFlowId())) {
                EContractEcSignReq ecSignReq = new EContractEcSignReq();
                BeanUtils.copyProperties(contractInfo, ecSignReq);
                ecSignReq.setLocalPdfUrl(contractInfo.getEcContractPathUrl());
                ecSignReq.setContractId(contractId);
                //生成电子合同
                String flowId = contractService.signEqbElectronicSeal(configInfo, ecSignReq, vasInfo);
                contractInfo.setEcContractEsignFlowId(flowId);
            }
            //下载合同
            String ecPdfUrl = eqbSignService.getDownloadDocumentUrl(configInfo, contractInfo.getEcContractEsignFlowId());
            if (StrUtil.isBlank(ecPdfUrl)) {
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(),FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_DOWNLOAD_FAIL.getCode());
                throw new BusinessException(errorCode,ContractErrorCode.FILE_DOWNLOAD_FAIL.getMessage());
            }
            //上传到文件服务器
            FileInfoOut fileInfo = fileHelper.urlUploadFile(ecPdfUrl, sessionInfo.getAgentCode(), DicConstant.DOCUMENT_TYPE.CONTRACT.toString() + ".pdf");
            if(fileInfo == null){
                Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(),FunctionCode.CON_THIRD.getCode(), ContractErrorCode.FILE_UPLOAD_FAIL.getCode());
                throw new BusinessException(errorCode,ContractErrorCode.FILE_UPLOAD_FAIL.getMessage());
            }
            contractPath.setEcontractUrl(fileInfo.getFileUrl());
            contractPath.setResultCode(ContractResult.XIAONIU_CONTRACT_BACK_0.getCode());
            contractPath.setResultDesc(ContractResult.XIAONIU_CONTRACT_BACK_0.getMsg());
            //保存电子印章ID
            contractService.saveEcPdfToContract(fileInfo.getFileID(), contractId);
        } catch (BusinessException e) {
            ContractResult contractResult = new ContractResult();
            contractResult.setCode(e.getCode());
            contractResult.setMsg(e.getMessage());
            if (ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode() == e.getCode()) {
                contractResult.setMsg("电子签章生成失败,原因：" + e.getMessage());
            }
            contractService.saveErrorInfoToContract(contractResult,contractInfo.getId());
            contractPath.setResultCode(e.getCode());
            contractPath.setResultDesc(e.getMessage());
            log.error("电子签章生成失败",e);
        } catch (Exception e) {
            log.error("框架合同电子签章异常：" + e.getMessage(),e);
            Integer errorCode = ServerCode.getServerCode(ServerCode.CONTRACT, ModuleCode.DETAIL.getCode(),FunctionCode.CON_THIRD.getCode(), ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getCode());
            throw new BusinessException(errorCode,ContractErrorCode.XIAONIU_CONTRACT_BACK_ERROR.getMessage());
        }
        return contractPath;
    }

    @Override
    public List<PartnerFraContractVO> selectPartnerFraContractList(FrameContractPartnerReq partnerReq) {
        if (partnerReq == null) {
            log.error("查询伙伴框架合同列表参数不能为空");
            return null;
        }
        if (CollUtil.isEmpty(partnerReq.getMemberCompanyIds())) {
            log.error("查询伙伴框架合同列表伙伴公司ID集合不能为空");
            return null;
        }
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<>();
        if (DicConstant.MEMBER_TYPE.TRUSTOR.equals(partnerReq.getMemberType())) {
            queryWrapper.in(Contract::getTrustorCompanyId,partnerReq.getMemberCompanyIds())
                    .eq(ObjectUtils.isNotEmpty(partnerReq.getCompanyId()),Contract::getCarrierCompanyId,partnerReq.getCompanyId())
                    .eq(Contract::getContractType,DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT)
                    .ne(Contract::getIsDelete,DicConstant.IS_DELETE.YES);
        } else {
            queryWrapper.in(Contract::getCarrierCompanyId,partnerReq.getMemberCompanyIds())
                    .eq(ObjectUtils.isNotEmpty(partnerReq.getCompanyId()),Contract::getTrustorCompanyId,partnerReq.getCompanyId())
                    .eq(Contract::getContractType,DicConstant.CONTRACT_TYPE.FRAMEWORK_CONTRACT)
                    .ne(Contract::getIsDelete,DicConstant.IS_DELETE.YES);
        }
        List<Contract> contractList = contractMapper.selectList(queryWrapper);
        List<PartnerFraContractVO> partnerFraContractList = new ArrayList<>();
        for (Contract contract : contractList) {
            PartnerFraContractVO partnerFraContract = new PartnerFraContractVO();
            partnerFraContract.setContractId(contract.getId());
            partnerFraContract.setContractCode(contract.getContractCode());
            partnerFraContract.setTrustorCompanyId(contract.getTrustorCompanyId());
            partnerFraContract.setCarrierCompanyId(contract.getCarrierCompanyId());
            partnerFraContractList.add(partnerFraContract);
        }
        return partnerFraContractList;
    }

    /**
     * 保存本地框架合同Pdf
     * @param fraCont
     * @return
     * @throws Exception
     */
    @Override
    public ContractResult createLocalFramePdf(FrameContractDtlQryVO fraCont) {
        ContractResult result = new ContractResult();
        if (fraCont == null) {
            log.error("生成本地框架合同PDF的合同不存在");
            result = new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getMsg());
            return result;
        }
        String trustorCode = organizationOrSettingHelper.getKeyWord(fraCont.getTrustorCompany());
        String carrierCode = organizationOrSettingHelper.getKeyWord(fraCont.getCarryCompany());
        if(StrUtil.isBlank(trustorCode) || StrUtil.isBlank(carrierCode)){
            log.info("找不到签署双方的关键字！");
            result = new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_ERROR.getMsg());
            return result;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("contract",fraCont);
        map.put("firstKeyWord",trustorCode);
        map.put("secondKeyWord",carrierCode);
        log.info("firstKeyWord是：{}，secondKeyWord是：{}",trustorCode,carrierCode);
        String htmlTempl = PdfHelper.changeFtlToHtml(map, fraCont.getContractType(), DicConstant.CONTRACT_BUSINESS_TYPE.BOTH);
        if (StrUtil.isBlank(htmlTempl)) {
            result.setCode(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120001.getCode()+""));
            result.setMsg(ContractResult.XIAONIU_CONTRACT_BACK_120001.getMsg());
            return result;
        }
        /*log.info(htmlTempl);*/
        FileInfoOut fileInfo = fileHelper.htmlToPdf(htmlTempl, LoginUserContextHolder.getUser().getAgentCode());
        if (fileInfo == null) {
            result.setCode(Integer.valueOf(ContractResult.XIAONIU_CONTRACT_BACK_120002.getCode()+""));
            result.setMsg(ContractResult.XIAONIU_CONTRACT_BACK_120002.getMsg());
            return result;
        }
        contractService.saveLocalPdfToContract(fileInfo.getFileID(),fraCont.getId());
        //pdf路径
        fraCont.setEcContractPathUrl(fileInfo.getFileUrl());
        return new ContractResult(ContractResult.XIAONIU_CONTRACT_BACK_0.getCode(),ContractResult.XIAONIU_CONTRACT_BACK_0.getMsg());
    }

    @Override
    public List<ContractFrameRsp> getContractFrame(ContractFrameReq contractFrameReq) {
        log.info("查询框架合同及附件信息入参:"+JSON.toJSONString(contractFrameReq));
        List<ContractFrameRsp> contractFrames = contractMapper.getContractFrame(contractFrameReq);

        if (contractFrames != null && contractFrames.size() != 0) {

            for (ContractFrameRsp contractFrameRsp : contractFrames) {
                //附件信息
                List<ContractAttach> contractAttachList = getContractAttachInfoByContractIds(Arrays.asList(contractFrameRsp.getContractId()));
                List<FileInfo> fileInfoList = getContractFileInfos(contractAttachList);
                contractFrameRsp.setFileInfos(fileInfoList);
                if (fileInfoList != null && fileInfoList.size() != 0){
                    // 框架合同有且只有一个合同附件 by xw
                    contractFrameRsp.setContractUrl(fileInfoList.get(0).getFileUrl());
                }

            }
        }

        log.info("查询框架合同及附件信息结果:"+JSON.toJSONString(contractFrames));
        return contractFrames;
    }

    @Override
    public ContractFrameRsp getContractFile(String id) {

        Contract contract = contractMapper.selectById(id);

        ContractFrameRsp contractFrameRsp = new ContractFrameRsp();
        //附件信息
        List<ContractAttach> contractAttachList = getContractAttachInfoByContractIds(Arrays.asList(contract.getId()));
        List<FileInfo> fileInfoList = getContractFileInfos(contractAttachList);
        contractFrameRsp.setFileInfos(fileInfoList);
        if (fileInfoList != null && fileInfoList.size() != 0) {
            // 框架合同有且只有一个合同附件 by xw
            contractFrameRsp.setContractUrl(fileInfoList.get(0).getFileUrl());
        }

        return contractFrameRsp;
    }

    /**
     * 校验框架合同创建入参
     * @param inParam
     * @return
     */
    private boolean checkFrameContractCreParam(FrameContractCreUpdReq inParam) {
        if (inParam == null) {
            return false;
        }
        if (StrUtil.isBlank(inParam.getContractCode())) {
            log.info("框架合同编号不能为空！");
            return false;
        }
//        if (StrUtil.isBlank(inParam.getTitle())) {
//            log.info("框架合同标题不能为空！");
//            return false;
//        }
//        if (StrUtil.isBlank(inParam.getContent())) {
//            log.info("框架合同内容不能为空！");
//            return false;
//        }
//        if (StrUtil.isBlank(inParam.getTemplateName())) {
//            log.info("模板名称不能为空！");
//            return false;
//        }
        if (StrUtil.isBlank(inParam.getTrustorCompanyId())) {
            log.info("托运方不能为空！");
            return false;
        }
        if (StrUtil.isBlank(inParam.getCarrierCompanyId())) {
            log.info("承运方不能为空！");
            return false;
        }
        if(inParam.getTrustorCompanyId().equals(inParam.getCarrierCompanyId())){
            log.info("合同双方不能是同一个公司！");
            return false;
        }
        if (null == inParam.getStartDate()) {
            return false;
        }
        if (null == inParam.getEndDate()) {
            return false;
        }
        return true;
    }

    /**
     * 添加合同附件
     * @param contractId
     * @param fileIds
     * @param sessionInfo
     */
    public void addContractAttach(String contractId,List<String> fileIds,SysUser sessionInfo) {
        if(CollUtil.isEmpty(fileIds)){
            return;
        }
        //逻辑删除当前合同下的所有首页相关文件
        attachMapper.updateContractAttachStatus(contractId);
        //添加附件
        String userId = sessionInfo.getId();
        for (String fileId : fileIds) {
            ContractAttach attach = new ContractAttach();
            attach.setId(IdWorker.getSnowflakeId(SnowflakeEnum.CONTRACT));
            attach.setContractId(contractId);
            attach.setFileId(fileId);
            attach.setCreater(userId);
            attach.setModifier(userId);
            attach.setCreateTime(new Date());
            attach.setModifyTime(new Date());
            attach.setVer(1);
            attachMapper.insert(attach);
        }

    }
}
