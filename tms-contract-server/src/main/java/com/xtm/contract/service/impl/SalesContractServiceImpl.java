package com.xtm.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.common.service.impl.SuperServiceImpl;
import com.xtm.company.feign.CompanyFeign;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.SalesContractHistoryOperationTypeEnum;
import com.xtm.contract.enums.SalesContractTypeEnum;
import com.xtm.contract.enums.PriceTypeEnum;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.enums.SaleTypeEnum;
import com.xtm.contract.enums.SystemSourceEnum;
import com.xtm.contract.mapper.FrameAgreementSubMapper;
import com.xtm.contract.mapper.SalesContractGoodsMapper;
import com.xtm.contract.mapper.SalesContractMapper;
import com.xtm.contract.mapper.SalesContractTermsMapper;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.FrameAgreement;
import com.xtm.contract.model.domain.FrameAgreementSub;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.domain.SalesContractGoods;
import com.xtm.contract.model.domain.SalesContractTerms;
import com.xtm.contract.model.dto.GoodsQuantityDTO;
import com.xtm.contract.model.dto.HistoryDTO;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import com.xtm.contract.model.dto.contract.SalesContractHistorySaveDTO;
import com.xtm.contract.model.enums.FrameAgreementHistoryOperationTypeEnum;
import com.xtm.contract.model.param.ContractGoodsOrdersMainQuantityParam;
import com.xtm.contract.model.param.NcSalesContractChangeParam;
import com.xtm.contract.model.param.SalesContractContext;
import com.xtm.contract.model.param.SalesContractListParam;
import com.xtm.contract.model.param.NcSalesContractAddOrUpdateParam;
import com.xtm.contract.model.param.NcSalesContractGoodsAddParam;
import com.xtm.contract.model.param.NcSalesContractTermsAddParam;
import com.xtm.contract.model.param.NcUpdateStateParam;
import com.xtm.contract.model.param.UpdateAccumulateOrdersMainQuantityParam;
import com.xtm.contract.model.param.UpdateStatusParam;
import com.xtm.contract.model.vo.SalesContractDetailVO;
import com.xtm.contract.model.vo.SalesContractGoodsVO;
import com.xtm.contract.model.vo.SalesContractTermsVO;
import com.xtm.contract.model.vo.SalesContractVo;
import com.xtm.contract.model.vo.SalesContractListVO;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementHistoryFieldVo;
import com.xtm.contract.service.FrameAgreementService;
import com.xtm.contract.service.InternalMatchingApiLogService;
import com.xtm.contract.service.SalesContractHistoryService;
import com.xtm.contract.service.SalesContractGoodsService;
import com.xtm.contract.service.SalesContractTermsService;
import com.xtm.contract.service.SalesContractService;
import com.xtm.contract.utils.NumberUtil;
import com.xtm.lock.core.ZLock;
import com.xtm.lock.redis.RedissonDistributedLock;
import com.xtm.setting.feign.SettingFeign;
import com.xtm.setting.model.dto.CargoOwnerInfoParam;
import com.xtm.setting.model.vo.CargoOwnerInfoResult;
import com.xtm.thirdparty.data.feign.IThirdPartDataFeign;
import com.xtm.thirdparty.data.model.dto.TrayCustomerDto;
import com.xtm.thirdparty.data.model.vo.ProductLineVo;
import com.xtm.thirdparty.data.model.vo.TrayCustomerVo;
import com.xtm.utils.json.JsonUtils;
import com.xtm.v1.common.model.TransitionBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  19:56
 *@Description: 物流合同接口实现类
 */
@Service
@Slf4j
public class SalesContractServiceImpl extends SuperServiceImpl<SalesContractMapper, SalesContract>
        implements SalesContractService {

    @Resource
    private SalesContractMapper salesContractMapper;

    @Resource
    private SalesContractGoodsMapper salesContractGoodsMapper;

    @Resource
    private SettingFeign settingFeign;

    @Resource
    private CompanyFeign companyFeign;

    @Resource
    private IThirdPartDataFeign thirdPartDataFeign;

    @Resource
    private SalesContractTermsMapper salesContractTermsMapper;

    @Resource
    private FrameAgreementService frameAgreementService;

    @Resource
    private FrameAgreementSubMapper frameAgreementSubMapper;

    @Resource
    private SalesContractGoodsService salesContractGoodsService;

    @Resource
    private SalesContractTermsService salesContractTermsService;

    @Resource
    private SalesContractHistoryService logisticContractHistoryService;

    @Resource
    private RedissonDistributedLock redissonDistributedLock;

    @Resource
    private InternalMatchingApiLogService internalMatchingApiLogService;


    /**
     * 根据物流合同查询启用且未绑定物流合同协议的物流合同接口
     *
     * @param contractCode 合同的编号
     */
    @Override
    public List<SalesContractVo> getEnableUnRelateContractByCode(String contractCode) {
        return salesContractMapper.getEnableUnRelateContractByCode(contractCode);
    }

    /**
     * NC更新累计订单主数量
     * 更新同一个NC主键下的所有版本
     */
    @Override
    public Result<String> updateAccumulateOrdersMainQuantity(UpdateAccumulateOrdersMainQuantityParam param) {
        Result<String> result = checkNcUpdateOrdersMainQuantityParam(param);
        if(!result.isSuccess()){
            return result;
        }

        String salesContractId = param.getSalesContractId();
        LambdaQueryWrapper<SalesContract> contractQueryWrapper = new LambdaQueryWrapper<>();
        contractQueryWrapper.eq(SalesContract::getSalesContractId, salesContractId)
                .eq(SalesContract::getDeleted, false);
        List<SalesContract> contractList = list(contractQueryWrapper);
        if (CollUtil.isEmpty(contractList)) {
            log.error("NC更新累计订单主数量-销售合同不存在:{}",JSONUtil.toJsonStr(param));
            return Result.error("销售合同不存在");
        }

        List<Long> ids = contractList.stream().map(TransitionBaseEntity::getId).collect(Collectors.toList());
        List<ContractGoodsOrdersMainQuantityParam> goodsQuantityList = param.getGoodsQuantityList();
        //转为Map
        Map<String, ContractGoodsOrdersMainQuantityParam> contractGoodsIdMap = goodsQuantityList.stream().collect(Collectors.toMap(ContractGoodsOrdersMainQuantityParam::getContractGoodsId, v -> v));
        List<String> contractGoodsIds = goodsQuantityList.stream().map(ContractGoodsOrdersMainQuantityParam::getContractGoodsId).collect(Collectors.toList());
        LambdaQueryWrapper<SalesContractGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SalesContractGoods::getSalesContractId, ids)
                .in(SalesContractGoods::getContractGoodsId, contractGoodsIds)
                .eq(SalesContractGoods::getDeleted, false);
        List<SalesContractGoods> contractGoodsList = salesContractGoodsMapper.selectList(queryWrapper);
        SysUser sysUser = LoginUserContextHolder.getUser();
        for (SalesContractGoods salesContractGoods : contractGoodsList) {
            String contractGoodsId = salesContractGoods.getContractGoodsId();
            if (contractGoodsIdMap.containsKey(contractGoodsId)) {
                ContractGoodsOrdersMainQuantityParam contractGoodsOrdersMainQuantityParam = contractGoodsIdMap.get(contractGoodsId);
                LambdaUpdateWrapper<SalesContractGoods> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(SalesContractGoods::getId, salesContractGoods.getId());
                updateWrapper.set(SalesContractGoods::getMainOrdersQuantity, contractGoodsOrdersMainQuantityParam.getMainOrdersQuantity())
                        .set(SalesContractGoods::getModifyId, sysUser.getId())
                        .set(SalesContractGoods::getModifyName, sysUser.getName())
                        .set(SalesContractGoods::getModifyTime, LocalDateTime.now());
                salesContractGoodsMapper.update(null, updateWrapper);
            }
        }
        return Result.ok();
    }

    /**
     * nc更新累计订单主数量接口参数校验
     */
    private Result<String> checkNcUpdateOrdersMainQuantityParam(UpdateAccumulateOrdersMainQuantityParam param) {
        String salesContractId = param.getSalesContractId();
        if(com.xtm.utils.string.StringUtils.isBlank(salesContractId)){
            return Result.error(ResultCode.VALIDATOR.getCode(), "销售合同id不能为空");
        }
        List<ContractGoodsOrdersMainQuantityParam> goodsQuantityList = param.getGoodsQuantityList();
        if(CollUtil.isEmpty(goodsQuantityList)){
            return Result.error(ResultCode.VALIDATOR.getCode(), "销售合同货物信息不能为空");
        }
        for (ContractGoodsOrdersMainQuantityParam contractGoodsOrdersMainQuantityParam : goodsQuantityList) {
            String contractGoodsId = contractGoodsOrdersMainQuantityParam.getContractGoodsId();
            if(com.xtm.utils.string.StringUtils.isBlank(contractGoodsId)){
                return Result.error(ResultCode.VALIDATOR.getCode(), "销售合同货物id不能为空");
            }
            BigDecimal mainOrdersQuantity = contractGoodsOrdersMainQuantityParam.getMainOrdersQuantity();
            if(mainOrdersQuantity == null){
                return Result.error(ResultCode.VALIDATOR.getCode(), "销售合同货物累计订单主数量不能为空");
            }
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> ncCreate(NcSalesContractAddOrUpdateParam param) {
        log.info("nc创建销售合同参数:{}", JsonUtils.toJSONString(param));
        String contractCode = param.getContractCode();
        if (StrUtil.isBlank(contractCode)) {
            log.error("销售合同编号不能为空:{}", JSONUtil.toJsonStr(param));
            throw new BusinessException("NC销售合同编号不能为空");
        }
        String salesContractId = param.getSalesContractId();
        String idKey = Constant.SAVE_UPDATE_SALES_CONTRACT_LOCK_KEY + salesContractId;
        String contractCodeKey = Constant.SAVE_UPDATE_SALES_CONTRACT_LOCK_KEY + contractCode;
        log.info("获取锁:{},{}", idKey, contractCodeKey);
        try (ZLock idLock = redissonDistributedLock.lock(idKey); ZLock contractCodeLock = redissonDistributedLock.lock(idKey)) {
            //校验NC销售合同参数
            Result<String> checkResult = checkNcSaveOrUpdateParam(param);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }

            //查询同版本销售合同是否已经存在
            SalesContract salesContract = checkContractExistsAndGetSameVersion(param);
            if(salesContract!=null){
                return updateContract(param, salesContract);
            }

            return ncCreateNewVersionContract(param);
        } catch (Exception e) {
            log.error("nc创建销售合同失败", e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 处理销售合同主表逻辑
     *
     * @param contract        合同主表信息
     * @param param           nc推送的参数
     * @param contractContext 上下文
     */
    private void handleSalesContract(SalesContract contract, NcSalesContractAddOrUpdateParam param,
                                     SalesContractContext contractContext) {
        Integer businessSource = getBusinessSource(param);
        contract.setBusiSource(businessSource);
        CompanyVo salesOrg = contractContext.getSalesOrg();
        CompanyVo customer = contractContext.getCustomer();
        ProductLineVo productLine = contractContext.getMainProductLine();
        //填充物流合同主表属性值
        SysUser sysUser = LoginUserContextHolder.getUser();
        //设置合同属性
        contract.setSalesContractId(param.getSalesContractId());
        if(salesOrg!=null){
            contract.setSalesOrgId(salesOrg.getId());
            contract.setSalesOrgName(salesOrg.getName());
        }
        contract.setSalesOrgUscc(param.getSalesOrganizationUscc());
        contract.setCode(param.getContractCode());
        contract.setName(param.getContractName());
        contract.setContractType(param.getContractType());
        contract.setSignedTime(param.getSignedTime());
        contract.setEffectiveTime(param.getEffectiveTime());
        contract.setEndTime(param.getEndTime());
        contract.setSalesman(param.getSalesman());
        contract.setDepartment(param.getDepartment());
        if(customer!=null){
            contract.setCustomerId(customer.getId());
            contract.setCustomerName(customer.getName());
        }
        contract.setCustomerUscc(param.getCustomerUscc());
        if(StringUtils.isNotBlank(param.getCustomerName())){
            contract.setCustomerName(param.getCustomerName());
        }
        contract.setTrayCustomerName(param.getTrayCustomerName());
        contract.setNcTrayCustomerId(param.getNcTrayCustomerId());
        if(productLine!=null){
            contract.setProductLineId(Long.parseLong(productLine.getId()));
            contract.setProductLineName(productLine.getProductLineName());
        }
        contract.setProductLineCode(param.getProductLineCode());
        contract.setSaleType(param.getSaleType());
        contract.setPriceType(param.getPriceType());
        contract.setAgreementDate(param.getAgreementDate());
        contract.setBearCostType(param.getBearCostType());
        contract.setDeparturePlace(param.getDeparturePlace());
        if (param.getIsDetainedGoods() != null) {
            contract.setDetainedGoods(param.getIsDetainedGoods() ? 1 : 0);
        }
        contract.setBusinessType(param.getBusinessType());
        contract.setWholeMeasurement(param.getIsWholeMeasurement());
        contract.setOceanCustomers(param.getIsOceanCustomers());
        contract.setTotalNumber(param.getTotalNumber());
        contract.setTotalPriceTax(param.getTotalPriceTax());
        contract.setPoundNote(param.getPoundNote());
        contract.setRemark(param.getRemark());
        contract.setDiscountYear(param.getDiscountYear());
        contract.setTwoFactoryTrade(param.getTwoFactoriesBus());
        contract.setTwoFactoryTradeCode(param.getTwoFactoriesBusCode());
        NcSalesContractChangeParam changeParam = getMaxVersionChange(param.getChangeList());
        contract.setNcAgreementCode(param.getYearAgreement());
        if(changeParam != null){
            contract.setContractVersion(changeParam.getVersion());
            contract.setChangePerson(changeParam.getChangePerson());
            contract.setChangeTime(changeParam.getChangeTime());
            contract.setChangeReason(changeParam.getChangeReason());
            contract.setChangeRemark(changeParam.getChangeRemark());
        }
        contract.setSystemSource(SystemSourceEnum.NC.getCode());

        contract.setModifyId(sysUser.getId());
        contract.setModifyName(sysUser.getName());
        contract.setModifyTime(LocalDateTime.now());

        contract.setDeleted(false);
        contract.setCreateId(sysUser.getId());
        contract.setCreateName(sysUser.getName());
        contract.setCreateTime(LocalDateTime.now());
        salesContractMapper.insert(contract);
    }

    /**
     * 处理销售合同主表逻辑
     *
     * @param sourceContract        合同主表信息
     * @param param           nc推送的参数
     * @param contractContext  上下文
     */
    private SalesContract updateSalesContract(SalesContract sourceContract, NcSalesContractAddOrUpdateParam param,
                                              SalesContractContext contractContext,HistoryDTO historyDTO) {
        LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SalesContract::getId, sourceContract.getId());
        Integer busiSource = getBusiSource(param, sourceContract);
        updateWrapper.set(SalesContract::getBusiSource, busiSource);

        SysUser sysUser = LoginUserContextHolder.getUser();
        CompanyVo salesOrg = contractContext.getSalesOrg();
        CompanyVo customer = contractContext.getCustomer();
        ProductLineVo productLine = contractContext.getMainProductLine();
        //填充物流合同主表属性值
        String saleOrgName = param.getSalesOrganization();
        if(salesOrg!=null){
            updateWrapper.
                    set(SalesContract::getSalesOrgId, salesOrg.getId());
            if (StringUtils.isBlank(saleOrgName)){
                saleOrgName = salesOrg.getName();
            }
        }

        String customerName = param.getCustomerName();
        if(customer!=null){
            updateWrapper.
                    set(SalesContract::getCustomerId, customer.getId());
            if (StringUtils.isBlank(customerName)){
                customerName = customer.getName();
            }
        }

        if (productLine!=null){
            updateWrapper.
                    set(SalesContract::getProductLineId, productLine.getId())
                    .set(SalesContract::getProductLineName, productLine.getProductLineName());
        }

        updateWrapper
                .set(SalesContract::getSalesOrgName, saleOrgName)
                .set(SalesContract::getSalesOrgUscc, param.getSalesOrganizationUscc())
                .set(SalesContract::getCode, param.getContractCode())
                .set(SalesContract::getName, param.getContractName())
                .set(SalesContract::getContractType, param.getContractType())
                .set(SalesContract::getSignedTime, param.getSignedTime())
                .set(SalesContract::getEffectiveTime, param.getEffectiveTime())
                .set(SalesContract::getEndTime, param.getEndTime())
                .set(SalesContract::getSalesman, param.getSalesman())
                .set(SalesContract::getDepartment, param.getDepartment())
                .set(SalesContract::getCustomerName, customerName)
                .set(SalesContract::getCustomerUscc, param.getCustomerUscc())
                .set(SalesContract::getTrayCustomerName, param.getTrayCustomerName())
                .set(SalesContract::getNcTrayCustomerId, param.getNcTrayCustomerId())
                .set(SalesContract::getProductLineCode, param.getProductLineCode())
                .set(SalesContract::getSaleType, param.getSaleType())
                .set(SalesContract::getPriceType, param.getPriceType())
                .set(SalesContract::getAgreementDate, param.getAgreementDate())
                .set(SalesContract::getBearCostType, param.getBearCostType())
                .set(SalesContract::getDeparturePlace, param.getDeparturePlace())
                .set(SalesContract::getDetainedGoods, param.getIsDetainedGoods())
                .set(SalesContract::getBusinessType, param.getBusinessType())
                .set(SalesContract::getWholeMeasurement, param.getIsWholeMeasurement())
                .set(SalesContract::getOceanCustomers, param.getIsOceanCustomers())
                .set(SalesContract::getTotalNumber, param.getTotalNumber())
                .set(SalesContract::getNcAgreementCode, param.getYearAgreement())
                .set(SalesContract::getTotalPriceTax, param.getTotalPriceTax())
                .set(SalesContract::getPoundNote, param.getPoundNote())
                .set(SalesContract::getRemark, param.getRemark())
                .set(SalesContract::getDiscountYear, param.getDiscountYear())
                .set(SalesContract::getTwoFactoryTrade, param.getTwoFactoriesBus())
                .set(SalesContract::getTwoFactoryTradeCode, param.getTwoFactoriesBusCode())
                .set(SalesContract::getDisabled, false)
                .set(SalesContract::getModifyId, sysUser.getId())
                .set(SalesContract::getModifyName, sysUser.getName())
                .set(SalesContract::getModifyTime, LocalDateTime.now());
        NcSalesContractChangeParam changeParam = getMaxVersionChange(param.getChangeList());
        if (changeParam!=null){
            updateWrapper.set(SalesContract::getChangePerson, changeParam.getChangePerson())
                    .set(SalesContract::getChangeTime, changeParam.getChangeTime())
                    .set(SalesContract::getChangeReason, changeParam.getChangeReason())
                    .set(SalesContract::getChangeRemark, changeParam.getChangeRemark());
        }
        salesContractMapper.update(null, updateWrapper);
        historyDTO.setBeforeSalesContract(sourceContract);
        SalesContract salesContract = getById(sourceContract.getId());
        historyDTO.setAfterSalesContract(salesContract);
        return salesContract;
    }

    /**
     * 获取最大版本号变更
     * 如果所有版本号为空，取最后一条变更作为最新
     * @param changeList 版本变更列表
     */
    private NcSalesContractChangeParam getMaxVersionChange(List<NcSalesContractChangeParam> changeList){
        if (CollUtil.isEmpty(changeList)){
            return null;
        }
        NcSalesContractChangeParam maxChange = changeList.get(0);
        Integer maxVersion = maxChange.getVersion();
        for (NcSalesContractChangeParam change : changeList) {
            Integer version = change.getVersion();
            if(maxVersion==null){
                maxChange = change;
            }else if (version!=null&&version>maxVersion){
                maxChange = change;
                maxVersion = version;
            }
        }
        return maxChange;
    }
    /**
     *
     * @param param
     * @param sourceContract
     * @return
     */
    private Integer getBusiSource(NcSalesContractAddOrUpdateParam param,SalesContract sourceContract){
        Integer busiSource = getBusinessSource(param);
        if(busiSource!=null){
            return busiSource;
        }

        //如果基地来源获取失败，则如果参数没有用变化则原来的基地来源保持不变
        String salesOrganizationUscc = StringUtils.isBlank(param.getSalesOrganizationUscc())?"":param.getSalesOrganizationUscc();
        NcSalesContractGoodsAddParam salesContractGoods = param.getSalesContractGoods().get(0);
        String shippingCompanyId = StringUtils.isBlank(salesContractGoods.getShippingCompanyId())?"":salesContractGoods.getShippingCompanyId();

        //查询货物信息
        List<SalesContractGoods> contractGoods = getContractGoods(sourceContract.getId());
        String sourceOrgUscc = StringUtils.isBlank(sourceContract.getSalesOrgUscc())? "":sourceContract.getSalesOrgUscc();
        String sourceShippingCompanyId = StringUtils.isBlank(contractGoods.get(0).getShippingCompanyId())?"":contractGoods.get(0).getShippingCompanyId();
        if (salesOrganizationUscc.equals(sourceOrgUscc)&&shippingCompanyId.equals(sourceShippingCompanyId)){
            //如果匹配基地来源参数发生变化则用新获取的基地来源
            return sourceContract.getBusiSource();
        }

        return busiSource;
    }

    /**
     * 根据合同id查询货物信息
     * @param contractId
     * @return
     */
    public List<SalesContractGoods> getContractGoods(Long contractId) {
        return salesContractGoodsMapper.selectList(new LambdaQueryWrapper<SalesContractGoods>()
                .eq(SalesContractGoods::getSalesContractId, contractId)
                .eq(SalesContractGoods::getDeleted, false));
    }
    /**
     * 获取基地业务来源
     */
    private Integer getBusinessSource(NcSalesContractAddOrUpdateParam param) {
        List<NcSalesContractGoodsAddParam> salesContractGoods = param.getSalesContractGoods();
        //获取基地来源
        return getAndSynBusiSource(param.getSalesOrganization(), salesContractGoods.get(0).getShippingCompanyId());
    }

    /**
     * nc创建销售合同货物相关逻辑处理
     *
     * @param contractId        销售合同id
     * @param param           nc创建销售合同参数
     * @param historyDTO      历史记录对象
     * @param contractContext 创建销售合同上下文对象
     */
    public void handleContractGoods(Long contractId, NcSalesContractAddOrUpdateParam param, HistoryDTO historyDTO, SalesContractContext contractContext) {
        List<NcSalesContractGoodsAddParam> salesContractGoods = param.getSalesContractGoods();
        if (CollUtil.isEmpty(salesContractGoods)){
            return;
        }
        ProductLineVo mainProductLine = contractContext.getMainProductLine();
        SysUser sysUser = LoginUserContextHolder.getUser();

        List<SalesContractGoods> contractGoodsList = new ArrayList<>();
        Map<String, ProductLineVo> productLineMap = new HashMap<>();
        String productLineCode = param.getProductLineCode();
        if (StringUtils.isNotBlank(productLineCode)){
            productLineMap.put(param.getProductLineCode(), mainProductLine);
        }
        Map<String, CompanyVo> shippingCompanyMap = contractContext.getShippingCompanyMap();
        for (NcSalesContractGoodsAddParam salesContractGood : salesContractGoods) {
            SalesContractGoods tmsContractGoods = new SalesContractGoods();
            tmsContractGoods.setId(IdWorker.getId());
            tmsContractGoods.setCreateId(sysUser.getId());
            tmsContractGoods.setCreateName(sysUser.getName());
            tmsContractGoods.setCreateTime(LocalDateTime.now());
            CompanyVo shippingCompany = shippingCompanyMap.get(salesContractGood.getShippingCompanyUscc());
            fillContrctGoodsFields(contractId, shippingCompany, productLineMap, salesContractGood, tmsContractGoods);
            contractGoodsList.add(tmsContractGoods);
        }

        if (CollUtil.isNotEmpty(contractGoodsList)) {
            salesContractGoodsService.saveBatch(contractGoodsList);
        }
        historyDTO.setAfterContractGoods(contractGoodsList);
    }

    /**
     * nc创建销售合同货物相关逻辑处理
     *
     * @param contractId        销售合同id
     * @param param           nc创建销售合同参数
     * @param historyDTO      历史记录对象
     * @param contractContext 创建销售合同上下文对象
     */
    public void updateContractGoods(Long contractId, NcSalesContractAddOrUpdateParam param, HistoryDTO historyDTO, SalesContractContext contractContext) {
        SysUser sysUser = LoginUserContextHolder.getUser();

        List<SalesContractGoods> insertList = new ArrayList<>();

        //查询旧数据
        LambdaQueryWrapper<SalesContractGoods> contractGoodsQueryWrapper = new LambdaQueryWrapper<SalesContractGoods>();
        contractGoodsQueryWrapper.eq(SalesContractGoods::getSalesContractId, contractId).eq(SalesContractGoods::getDeleted, false);
        List<SalesContractGoods> sourceContractGoodsList = salesContractGoodsMapper.selectList(contractGoodsQueryWrapper);
        historyDTO.setBeforeContractGoods(sourceContractGoodsList);

        //旧数据按照货物id转换为map
        Map<String, SalesContractGoods> sourceContractGoodsMap = sourceContractGoodsList.stream().collect(Collectors.toMap(SalesContractGoods::getContractGoodsId, v -> v));

        Map<String, ProductLineVo> productLineMap = new HashMap<>();
        if (StringUtils.isNotBlank(param.getProductLineCode())){
            ProductLineVo mainProductLine = contractContext.getMainProductLine();
            productLineMap.put(param.getProductLineCode(), mainProductLine);
        }
        Map<String, CompanyVo> shippingCompanyMap = contractContext.getShippingCompanyMap();
        List<NcSalesContractGoodsAddParam> newSalesContractGoods = param.getSalesContractGoods()==null?new ArrayList<>():param.getSalesContractGoods();
        for (NcSalesContractGoodsAddParam newSalesContractGoodsParam : newSalesContractGoods) {
            String contractGoodsId = newSalesContractGoodsParam.getContractGoodsId();
            SalesContractGoods sourceContractGoods = sourceContractGoodsMap.get(contractGoodsId);
            if (sourceContractGoods != null) {
                updateSalesContractGoods(sourceContractGoods,newSalesContractGoodsParam,productLineMap);
                sourceContractGoodsMap.remove(contractGoodsId);
            }else{
                //走新增逻辑
                SalesContractGoods tmsContractGoods = new SalesContractGoods();
                tmsContractGoods.setId(IdWorker.getId());
                tmsContractGoods.setCreateId(sysUser.getId());
                tmsContractGoods.setCreateName(sysUser.getName());
                tmsContractGoods.setCreateTime(LocalDateTime.now());
                CompanyVo shippingCompany = shippingCompanyMap.get(newSalesContractGoodsParam.getShippingCompanyUscc());
                fillContrctGoodsFields(contractId, shippingCompany, productLineMap, newSalesContractGoodsParam, tmsContractGoods);
                insertList.add(tmsContractGoods);
            }
        }

        if (CollUtil.isNotEmpty(insertList)) {
            //设置合同货物字典属性 质量等级和主单位
            salesContractGoodsService.saveBatch(insertList);
        }

        //删除旧数据
        Collection<SalesContractGoods> removeContractGoods = sourceContractGoodsMap.values();
        if (CollUtil.isNotEmpty(removeContractGoods)) {
            List<Long> contractGoodsIdList = removeContractGoods.stream().map(SalesContractGoods::getId).collect(Collectors.toList());
            LambdaUpdateWrapper<SalesContractGoods> contractGoodsUpdateWrapper = new LambdaUpdateWrapper<>();
            contractGoodsUpdateWrapper.in(SalesContractGoods::getId, contractGoodsIdList)
                    .set(SalesContractGoods::getDeleted, true)
                    .set(SalesContractGoods::getModifyId, sysUser.getId())
                    .set(SalesContractGoods::getModifyName, sysUser.getName())
                    .set(SalesContractGoods::getModifyTime, LocalDateTime.now());
            salesContractGoodsMapper.update(null, contractGoodsUpdateWrapper);
        }
        historyDTO.setAfterContractGoods(getContractGoods(contractId));
    }

    private void updateSalesContractGoods(SalesContractGoods sourceContractGoods, NcSalesContractGoodsAddParam newSalesContractGoodsParam,Map<String, ProductLineVo> productLineMap) {
        ProductLineVo productLine = getProductLine(newSalesContractGoodsParam.getProductLineCode(), productLineMap);
        SysUser sysUser = LoginUserContextHolder.getUser();

        LambdaUpdateWrapper<SalesContractGoods> contractGoodsUpdateWrapper = new LambdaUpdateWrapper<>();
        contractGoodsUpdateWrapper.eq(SalesContractGoods::getId, sourceContractGoods.getId());
        contractGoodsUpdateWrapper.set(SalesContractGoods::getLineNumber, newSalesContractGoodsParam.getLineNumber())
                .set(SalesContractGoods::getShippingCompanyId, newSalesContractGoodsParam.getShippingCompanyId())
                .set(SalesContractGoods::getShippingCompanyName, newSalesContractGoodsParam.getShippingCompany())
                .set(SalesContractGoods::getShippingCompanyUscc, newSalesContractGoodsParam.getShippingCompanyUscc())
                .set(SalesContractGoods::getNcShippingCompanyId, newSalesContractGoodsParam.getShippingCompanyId())
                .set(SalesContractGoods::getNcShippingCompany, newSalesContractGoodsParam.getShippingCompany())
                .set(SalesContractGoods::getReferenceMaterial, newSalesContractGoodsParam.getReferenceMaterial())
                .set(SalesContractGoods::getMaterialClassification, newSalesContractGoodsParam.getMaterialClassification())
                .set(SalesContractGoods::getMaterial, newSalesContractGoodsParam.getMaterial())
                .set(SalesContractGoods::getTexture, newSalesContractGoodsParam.getTexture())
                .set(SalesContractGoods::getProductLineId, Optional.ofNullable(productLine).map(s->Long.parseLong(productLine.getId())).orElse(null))
                .set(SalesContractGoods::getProductLineCode, newSalesContractGoodsParam.getProductLineCode())
                .set(SalesContractGoods::getProductLineName,Optional.ofNullable(productLine).map(ProductLineVo::getProductLineName).orElse(null))
                .set(SalesContractGoods::getQualityGrade, newSalesContractGoodsParam.getQualityGrade())
                .set(SalesContractGoods::getQuantity, newSalesContractGoodsParam.getQuantity())
                .set(SalesContractGoods::getMainQuantity, newSalesContractGoodsParam.getMainQuantity())
                .set(SalesContractGoods::getMainUnit, newSalesContractGoodsParam.getMainUnit())
                .set(SalesContractGoods::getConversionRate, newSalesContractGoodsParam.getConversionRate())
                .set(SalesContractGoods::getBasePrice, newSalesContractGoodsParam.getBasePrice())
                .set(SalesContractGoods::getReceivingLocation, newSalesContractGoodsParam.getReceivingLocation())
                .set(SalesContractGoods::getWharf, newSalesContractGoodsParam.getWharf())
                .set(SalesContractGoods::getMaterialSpecification, newSalesContractGoodsParam.getMaterialSpecification())
                .set(SalesContractGoods::getMainTaxUnitPrice, newSalesContractGoodsParam.getMainTaxUnitPrice())
                .set(SalesContractGoods::getTotalPriceTax, newSalesContractGoodsParam.getTotalPriceTax())
                .set(SalesContractGoods::getReceivingCountry, newSalesContractGoodsParam.getReceivingCountry())
                .set(SalesContractGoods::getTaxCode, newSalesContractGoodsParam.getTaxCode())
                .set(SalesContractGoods::getPriceComposition, newSalesContractGoodsParam.getPriceComposition())
                .set(SalesContractGoods::getRemark, newSalesContractGoodsParam.getRemark())
                .set(newSalesContractGoodsParam.getMainOrdersQuantity()!=null,SalesContractGoods::getMainOrdersQuantity, newSalesContractGoodsParam.getMainOrdersQuantity())
                .set(SalesContractGoods::getModifyId, sysUser.getId())
                .set(SalesContractGoods::getModifyName, sysUser.getName())
                .set(SalesContractGoods::getModifyTime, LocalDateTime.now());
                salesContractGoodsService.update(contractGoodsUpdateWrapper);
    }

    private void fillContrctGoodsFields(Long contractId, CompanyVo shippingCompany, Map<String, ProductLineVo> productLineMap,
                                        NcSalesContractGoodsAddParam salesContractGood, SalesContractGoods tmsContractGoods) {

        SysUser sysUser = LoginUserContextHolder.getUser();

        tmsContractGoods.setContractGoodsId(salesContractGood.getContractGoodsId());
        tmsContractGoods.setSalesContractId(contractId);
        tmsContractGoods.setLineNumber(salesContractGood.getLineNumber());
        tmsContractGoods.setShippingCompanyId(shippingCompany.getId());
        tmsContractGoods.setShippingCompanyName(salesContractGood.getShippingCompany());
        tmsContractGoods.setShippingCompanyUscc(shippingCompany.getUnifiedSocialCreditIdentifier());
        tmsContractGoods.setNcShippingCompanyId(salesContractGood.getShippingCompanyId());
        tmsContractGoods.setNcShippingCompany(salesContractGood.getShippingCompany());
        tmsContractGoods.setReferenceMaterial(salesContractGood.getReferenceMaterial());
        tmsContractGoods.setMaterialClassification(salesContractGood.getMaterialClassification());
        tmsContractGoods.setMaterial(salesContractGood.getMaterial());
        tmsContractGoods.setTexture(salesContractGood.getTexture());
        ProductLineVo productLine = getProductLine(salesContractGood.getProductLineCode(), productLineMap);
        tmsContractGoods.setProductLineId(Optional.ofNullable(productLine).map(s->Long.parseLong(s.getId())).orElse(null));
        tmsContractGoods.setProductLineCode(salesContractGood.getProductLineCode());
        tmsContractGoods.setProductLineName(Optional.ofNullable(productLine).map(ProductLineVo::getProductLineName).orElse( null));
        tmsContractGoods.setQualityGrade(salesContractGood.getQualityGrade());
        tmsContractGoods.setQuantity(salesContractGood.getQuantity());
        tmsContractGoods.setMainQuantity(salesContractGood.getMainQuantity());
        tmsContractGoods.setMainUnit(salesContractGood.getMainUnit());
        tmsContractGoods.setConversionRate(salesContractGood.getConversionRate());
        tmsContractGoods.setBasePrice(salesContractGood.getBasePrice());
        tmsContractGoods.setReceivingLocation(salesContractGood.getReceivingLocation());
        tmsContractGoods.setWharf(salesContractGood.getWharf());
        tmsContractGoods.setMaterialSpecification(salesContractGood.getMaterialSpecification());
        tmsContractGoods.setMainTaxUnitPrice(salesContractGood.getMainTaxUnitPrice());
        tmsContractGoods.setTotalPriceTax(salesContractGood.getTotalPriceTax());
        tmsContractGoods.setReceivingCountry(salesContractGood.getReceivingCountry());
        tmsContractGoods.setTaxCode(salesContractGood.getTaxCode());
        tmsContractGoods.setPriceComposition(salesContractGood.getPriceComposition());
        tmsContractGoods.setRemark(salesContractGood.getRemark());
        tmsContractGoods.setMainOrdersQuantity(salesContractGood.getMainOrdersQuantity());
        tmsContractGoods.setModifyId(sysUser.getId());
        tmsContractGoods.setModifyName(sysUser.getName());
        tmsContractGoods.setModifyTime(LocalDateTime.now());
    }

    /**
     * nc创建接口处理合同条款逻辑
     *
     * @param isUpdate   是否是更新操作
     * @param contractId   销售合同id
     * @param param      nc创建参数
     * @param historyDTO 历史记录DTO
     */
    private void handleContractTerms(boolean isUpdate, Long contractId, NcSalesContractAddOrUpdateParam param, HistoryDTO historyDTO) {
        //设置合同条款
        if (isUpdate) {
            LambdaQueryWrapper<SalesContractTerms> termsQueryWrapper = new LambdaQueryWrapper<SalesContractTerms>();
            termsQueryWrapper.eq(SalesContractTerms::getSalesContractId, contractId).eq(SalesContractTerms::getDeleted, false);
            List<SalesContractTerms> tmsTermsList = salesContractTermsMapper.selectList(termsQueryWrapper);

            historyDTO.setBeforeContractTerms(tmsTermsList);
            //删除旧数据
            List<Long> termsIdList = tmsTermsList.stream().map(SalesContractTerms::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(termsIdList)) {
                SysUser sysUser = LoginUserContextHolder.getUser();
                LambdaUpdateWrapper<SalesContractTerms> termsUpdateWrapper = new LambdaUpdateWrapper<>();
                termsUpdateWrapper.in(SalesContractTerms::getId, termsIdList)
                        .set(SalesContractTerms::getDeleted, true)
                        .set(SalesContractTerms::getModifyId, sysUser.getId())
                        .set(SalesContractTerms::getModifyName, sysUser.getName())
                        .set(SalesContractTerms::getModifyTime, LocalDateTime.now());
                salesContractTermsMapper.update(null, termsUpdateWrapper);
            }
        }
        List<SalesContractTerms> termsList = buildTermsFields(param, contractId);
        if (CollUtil.isNotEmpty(termsList)) {
            salesContractTermsService.saveBatch(termsList);
        }
        historyDTO.setAfterContractTerms(termsList);
    }
    /**
     * 填充合同条款属性
     *
     * @param param    nc推送参数
     * @param contractId 销售合同id
     */
    private List<SalesContractTerms> buildTermsFields(NcSalesContractAddOrUpdateParam param, Long contractId) {
        List<SalesContractTerms> termsList = new ArrayList<>();
        List<NcSalesContractTermsAddParam> salesContractTerms = param.getSalesContractTerms();
        if (CollUtil.isNotEmpty(salesContractTerms)) {
            SysUser sysUser = LoginUserContextHolder.getUser();
            for (NcSalesContractTermsAddParam salesContractTerm : salesContractTerms) {
                SalesContractTerms tmsContractTerms = new SalesContractTerms();
                termsList.add(tmsContractTerms);
                tmsContractTerms.setId(IdWorker.getId());
                tmsContractTerms.setSalesContractId(contractId);
                tmsContractTerms.setTermCode(salesContractTerm.getTermCode());
                tmsContractTerms.setTermName(salesContractTerm.getTermName());
                tmsContractTerms.setTermContent(salesContractTerm.getTermContent());
                tmsContractTerms.setCreateId(sysUser.getId());
                tmsContractTerms.setCreateName(sysUser.getName());
                tmsContractTerms.setCreateTime(LocalDateTime.now());
                tmsContractTerms.setModifyId(sysUser.getId());
                tmsContractTerms.setModifyName(sysUser.getName());
                tmsContractTerms.setModifyTime(LocalDateTime.now());
            }
        }
        return termsList;
    }

    /**
     * 更新托盘客户属性
     */
    private Result<String> matchTrayCustomer(NcSalesContractAddOrUpdateParam param, SalesContract contract) {
        String trayCustomerName = param.getTrayCustomerName();
        TrayCustomerDto trayCustomerDto = new TrayCustomerDto();
        trayCustomerDto.setTrayCustomer(trayCustomerName);
        log.info("获取托盘客户参数:{}", JsonUtils.toJSONString(trayCustomerDto));
        Result<List<TrayCustomerVo>> trayCustomerResult = thirdPartDataFeign.getTrayCustomerListByName(trayCustomerDto);
        log.info("获取托盘客户结果:{}", JsonUtils.toJSONString(trayCustomerResult));
        if (trayCustomerResult.isSuccess()) {
            List<TrayCustomerVo> trayCustomerList = trayCustomerResult.getData();
            if (CollUtil.isNotEmpty(trayCustomerList)) {
                TrayCustomerVo trayCustomer = trayCustomerList.get(0);
                LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(SalesContract::getId, contract.getId());
                updateWrapper.set(SalesContract::getTrayCustomerId, trayCustomer.getId());
                contract.setNcTrayCustomerId(trayCustomer.getNcTrayCustomerId());
                contract.setTrayCustomerId(Long.parseLong(trayCustomer.getId()));
                salesContractMapper.update(null, updateWrapper);
            } else {
                return Result.error("当前托盘客户（"+trayCustomerName+"）不存在");
            }
        } else {
            return Result.error("托盘客户匹配异常:"+trayCustomerResult.getMsg());
        }

        return Result.ok();
    }

    /**
     * 获取NC并同步基地业务来源
     * 获取系统来源：纵横、中铁,从货主信息里面查询系统来源,先根据销售组织匹配，匹配不到则用发运公司匹配
     */
    private Integer getAndSynBusiSource(String salesOrganization, String shippingCompanyId) {
        Integer busiSource = getBusiSourceBySalesOrganization(salesOrganization);
        if(busiSource==null){
            busiSource = getBusiSourceByShippingCompany(shippingCompanyId);
        }
        return busiSource;
    }
    /**
     * 获取NC业务来源
     * 获取系统来源：纵横、中铁,从货主信息里面查询系统来源,先根据销售组织匹配，匹配不到则用发运公司匹配
     */
    private Integer getBusiSourceBySalesOrganization(String salesOrganization) {
        if (StringUtils.isBlank(salesOrganization)){
            return null;
        }
        Integer businessSource = null;
        log.info("根据销售组织查询货主平台配置开始，入参:{}", salesOrganization);
        CargoOwnerInfoParam cargoOwnerParam = new CargoOwnerInfoParam();
        cargoOwnerParam.setOwnerName(salesOrganization);
        Result<List<CargoOwnerInfoResult>> settingResult = settingFeign.getCargoOwnerListByParam(cargoOwnerParam);
        log.info("根据销售组织查询货主平台配置结束:{}", JSONObject.toJSON(settingResult));
        if (settingResult.isSuccess() && settingResult.getData() != null) {
            List<CargoOwnerInfoResult> resultData = settingResult.getData();
            if (CollUtil.isNotEmpty(resultData)) {
                CargoOwnerInfoResult cargoOwner = resultData.get(0);
                Integer stockOrg = cargoOwner.getStockOrg();
                if(stockOrg!=null&&stockOrg==1){
                    String busiSource = cargoOwner.getBusiSource();
                    // 业务来源 0:丰南 1:中铁 2:中重 99:本平台
                    businessSource = Integer.valueOf(busiSource);
                    synBusiSourceBySalesOrganization(salesOrganization,businessSource);
                    return businessSource;
                }else{
                    log.error("货主平台根据销售组织匹配信息非库存组织,{},{}",salesOrganization, stockOrg);
                }
            }
        }
        return null;
    }

    /**
     * 同步基地业务来源字段
     * @param salesOrganization 销售组织名称
     * @param businessSource 基地业务来源
     */
    void synBusiSourceBySalesOrganization(String salesOrganization, Integer businessSource){
        //查询所有获取基地来源失败的合同
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(SalesContract::getBusiSource)
                .eq(SalesContract::getSalesOrgName, salesOrganization)
                .eq(SalesContract::getDeleted, 0);
        List<SalesContract> salesContracts = list(queryWrapper);
        if (CollUtil.isNotEmpty(salesContracts)){
            List<Long> ids = salesContracts.stream().map(SalesContract::getId).collect(Collectors.toList());
            log.info("根据销售组织更新销售合同基地来源信息,合同id:{},{}", ids, businessSource);
            LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(SalesContract::getId,ids)
                    .set(SalesContract::getBusiSource, businessSource);
            update(updateWrapper);
        }
    }

    /**
     * 同步基地业务来源字段
     * @param shippingCompanyId NC发运公司主键
     * @param businessSource 基地业务来源
     */
    void synBusiSourceByShippingCompany(String shippingCompanyId, Integer businessSource){
        //查询所有获取基地来源失败的合同
        List<SalesContract> salesContracts = salesContractMapper.getSalesContractNoBusiSourceByShippingCompany(shippingCompanyId);
        if (CollUtil.isNotEmpty(salesContracts)){
            List<Long> ids = salesContracts.stream().map(SalesContract::getId).collect(Collectors.toList());
            log.info("根据销售组织更新销售合同基地来源信息,合同id:{},{}", ids, businessSource);
            LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(SalesContract::getId,ids)
                    .set(SalesContract::getBusiSource, businessSource);
            update(updateWrapper);
        }
    }

    /**
     * 根据发运公司获取基地业务来源
     * @param shippingCompanyId NC发运公司主键
     */
    private Integer getBusiSourceByShippingCompany(String shippingCompanyId) {
        if (StringUtils.isBlank(shippingCompanyId)){
            return null;
        }
        Integer businessSource = null;
        log.info("根据发运公司查询货主平台配置开始，入参:{}", shippingCompanyId);
        Result<CargoOwnerInfoResult> goodsSettingResult = settingFeign.getCargoOwnerByOwnerCode(shippingCompanyId);
        log.info("根据发运公司查询货主平台配置结束:{}", JSONObject.toJSON(goodsSettingResult));
        if (goodsSettingResult.isSuccess() && goodsSettingResult.getData() != null) {
            CargoOwnerInfoResult resultData = goodsSettingResult.getData();
            Integer stockOrg = resultData.getStockOrg();
            if(stockOrg!=null&&stockOrg==1){
                String busiSource = resultData.getBusiSource();
                // 业务来源 0:丰南 1:中铁 2:中重 99:本平台
                businessSource = Integer.valueOf(busiSource);
                synBusiSourceByShippingCompany(shippingCompanyId,businessSource);
            }else{
                log.error("货主平台根据发运公司主键匹配信息非库存组织,{},{}",shippingCompanyId, stockOrg);
            }
        }
        return businessSource;
    }

    private Result<FrameAgreement> checkCanRelate(NcSalesContractAddOrUpdateParam ncParam){
        String yearAgreement = ncParam.getYearAgreement();

        log.info("nc物流合同保存结束,开始绑定协议合同关系");
        LambdaQueryWrapper<FrameAgreement> agreementQueryWrapper = new LambdaQueryWrapper<>();
        //nc推送使用编码查询
        agreementQueryWrapper.eq(FrameAgreement::getCode, yearAgreement)
                .eq(FrameAgreement::getDeleted, 0);
        List<FrameAgreement> frameAgreements = frameAgreementService.list(agreementQueryWrapper);
        if (CollUtil.isEmpty(frameAgreements)) {
            log.info("关联框架协议失败,编码为:{}协议不存在", yearAgreement);
            return Result.error("当前协议号（"+yearAgreement+"）不存在");
        }
        if (frameAgreements.size() > 1){
            log.info("关联框架协议失败,编码为:{}协议存在多个", yearAgreement);
            return Result.error("当前协议号（"+yearAgreement+"）存在多个");
        }
        FrameAgreement frameAgreement = frameAgreements.get(0);
        Boolean disabled = frameAgreements.get(0).getDisabled();
        if (disabled){
            log.info("关联框架协议失败,编码为:{}协议已禁用", yearAgreement);
            return Result.error("当前协议号（"+yearAgreement+"）已禁用");
        }
        return Result.ok(frameAgreement);
    }
    /**
     * 关联框架协议
     *
     * @param contract  销售合同信息
     * @param agreement 框架协议信息
     */
    private void relateAgreement(SalesContract contract, FrameAgreement agreement,SalesContractContext contractContext) {
        LocalDateTime effectiveTime = contract.getEffectiveTime();
        Long agreementId = agreement.getId();
        Long saleContractId = contract.getId();
        SysUser sysUser = LoginUserContextHolder.getUser();
        String reMonth = null;
        if(effectiveTime!=null){
            reMonth = DateUtil.format(effectiveTime, "yyyy-MM");
        }
        //查询是否存在关联的子表
        String ncSalesContractId = contract.getSalesContractId();
        FrameAgreementSub agreementSub = frameAgreementSubMapper.getExistSub(ncSalesContractId);
        //isRelate = true 表示子表框架合同协议号和月份和销售合同一直 可以直接修改
        boolean isRelate = false;
        //子表存在的情况下是否需要更新
        boolean subNeedUpdate = true;
        if (agreementSub!=null) {
            String month = agreementSub.getMonth();
            if (StringUtils.isBlank(month)){
                month = null;
            }
            if (StringUtils.isBlank(reMonth)){
                reMonth = null;
            }
            Long lastAgreementId = agreementSub.getAgreementId();
            if(Objects.equals(lastAgreementId, agreementId)&&Objects.equals(month, reMonth)){
                isRelate = true;
                Long lastSaleContractId = Long.valueOf(agreementSub.getSaleContractId());
                if (Objects.equals(lastSaleContractId, saleContractId)){
                    //修改的场景
                    subNeedUpdate = false;
                }
            }else{
                //取消这个关联关系
                LambdaUpdateWrapper<FrameAgreementSub> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(FrameAgreementSub::getId, agreementSub.getId())
                        .set(FrameAgreementSub::getSaleContractId, null)
                        .set(FrameAgreementSub::getRelate, DicConstant.NumEnums.NUM_ZERO.getIntegerValue())
                        .set(FrameAgreementSub::getModifyId, sysUser.getId())
                        .set(FrameAgreementSub::getModifyTime, LocalDateTime.now())
                        .set(FrameAgreementSub::getModifyName, sysUser.getId());
                frameAgreementSubMapper.update(null, updateWrapper);
            }

            if(!agreementId.equals(lastAgreementId)){
                FrameAgreement lastFrameAgreement = frameAgreementService.getById(lastAgreementId);
                //协议号变更 框架合同记录取消关联的日志
                FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
                beforeAgreement.setReContractCode(contract.getCode());
                beforeAgreement.setCode(lastFrameAgreement.getCode());
                FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
                afterAgreement.setReContractCode(Constant.DEFAULT_VALUE);
                afterAgreement.setCode(lastFrameAgreement.getCode());
                contractContext.setCancelBeforeAgreement(beforeAgreement);
                contractContext.setCancelAfterAgreement(afterAgreement);
            }
        }
        LocalDateTime modifyTime = LocalDateTime.now();

        if(!isRelate){
            LambdaUpdateWrapper<FrameAgreementSub> subWrapper = new LambdaUpdateWrapper<>();
            //查询新的子表
            subWrapper.eq(FrameAgreementSub::getAgreementId, agreementId);
            subWrapper.eq(FrameAgreementSub::getRelate, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
            if (reMonth==null){
                subWrapper.isNull(FrameAgreementSub::getMonth);
            }else{
                subWrapper.eq(FrameAgreementSub::getMonth, reMonth);
            }
            subWrapper.eq(FrameAgreementSub::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue());
            subWrapper.last("limit 1");
            agreementSub = frameAgreementSubMapper.selectOne(subWrapper);
        }

        if (agreementSub != null&&subNeedUpdate) {
            LambdaUpdateWrapper<FrameAgreementSub> subUpdateWrapper = new LambdaUpdateWrapper<>();
            subUpdateWrapper.set(FrameAgreementSub::getRelate, 1);
            subUpdateWrapper.set(FrameAgreementSub::getSaleContractId, saleContractId);
            subUpdateWrapper.set(FrameAgreementSub::getModifyId, sysUser.getId());
            subUpdateWrapper.set(FrameAgreementSub::getModifyTime, modifyTime);
            subUpdateWrapper.set(FrameAgreementSub::getModifyName, sysUser.getName());
            subUpdateWrapper.eq(FrameAgreementSub::getId, agreementSub.getId());
            frameAgreementSubMapper.update(null, subUpdateWrapper);
        }

        //查询已经存在的关联的销售合同
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getSalesContractId, ncSalesContractId)
                .isNotNull(SalesContract::getFrameAgreementId)
                .eq(SalesContract::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue())
                .last("limit 1");
        SalesContract lastContract = salesContractMapper.selectOne(queryWrapper);

        //框架协议和销售合同建立关联关系
        LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SalesContract::getFrameAgreementId, agreementId);
        updateWrapper.set(SalesContract::getReMonth, reMonth);
        if (agreementSub != null) {
            updateWrapper.set(SalesContract::getReRemark, agreementSub.getRemark());
            updateWrapper.set(SalesContract::getReMergeState, agreementSub.getMergeState());
        }else{
            updateWrapper.set(SalesContract::getReMergeState, 0);
        }
        updateWrapper.set(SalesContract::getFrameAgreementCode, agreement.getCode());
        updateWrapper.set(SalesContract::getModifyId, sysUser.getId());
        updateWrapper.set(SalesContract::getModifyTime, modifyTime);
        updateWrapper.set(SalesContract::getModifyName, sysUser.getName());
        updateWrapper.eq(SalesContract::getId, saleContractId);
        salesContractMapper.update(null, updateWrapper);

        log.info("关联物流协议成功,销售合同id为:{},协议合同id为:{}", saleContractId, agreementId);


        if (lastContract!=null){
            Long lastContractId = lastContract.getId();
            if (!lastContractId.equals(saleContractId)){
                //创建新版版的场景 取消其他版本已经关联框架合同关系
                cancelContractRelate(lastContract);
            }

            Long lastAgreementId = lastContract.getFrameAgreementId();
            if (Objects.equals(lastAgreementId, agreementId)){
                //关联关系没有发生变化，不需要重复记录关联日志
                return;
            }
        }

        //框架合同记录关联日志
        FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
        beforeAgreement.setReContractCode(Constant.DEFAULT_VALUE);
        beforeAgreement.setCode(agreement.getCode());
        FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
        afterAgreement.setReContractCode(contract.getCode());
        afterAgreement.setCode(agreement.getCode());
        contractContext.setRelateBeforeAgreement(beforeAgreement);
        contractContext.setRelateAfterAgreement(afterAgreement);
    }

    /**
     * 销售合同取消关联框架协议
     */
    private void cancelContractRelate(SalesContract contract) {
        if (contract==null) {
            return;
        }
        SysUser sysUser = LoginUserContextHolder.getUser();
        Long id = contract.getId();
        LambdaUpdateWrapper<SalesContract> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SalesContract::getId, id)
                .set(SalesContract::getFrameAgreementId, null)
                .set(SalesContract::getFrameAgreementCode, null)
                .set(SalesContract::getReMonth, null)
                .set(SalesContract::getReMergeState, null)
                .set(SalesContract::getReRemark, null)
                .set(SalesContract::getModifyId, sysUser.getId())
                .set(SalesContract::getModifyTime, LocalDateTime.now())
                .set(SalesContract::getModifyName, sysUser.getName());
        salesContractMapper.update(null, updateWrapper);
    }

    /**
     * 获取产品线信息
     */
    private ProductLineVo getProductLine(String productLineCode, Map<String, ProductLineVo> productLineMap) {
        ProductLineVo productLineVo = productLineMap.get(productLineCode);
        if (productLineVo != null) {
            return productLineVo;
        }
        Result<ProductLineVo> lineResult = thirdPartDataFeign.getProductLineByCode(productLineCode);
        if (lineResult.isSuccess() && lineResult.getData() != null) {
            productLineVo = lineResult.getData();
            productLineMap.put(productLineCode, productLineVo);
            return productLineVo;
        }
        return null;
    }

    /**
     * 校验销售合同是否存在并返回版本号一致的合同
     */
    private SalesContract checkContractExistsAndGetSameVersion(NcSalesContractAddOrUpdateParam param) {
        Integer maxVersion = null;
        NcSalesContractChangeParam changeParam = getMaxVersionChange(param.getChangeList());
        if (changeParam != null){
            maxVersion = changeParam.getVersion();
        }
        //查询合同信息
        String salesContractId = param.getSalesContractId();
        //获取相同合同主键的最新版本的数据
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getSalesContractId, salesContractId)
                .eq(maxVersion!=null,SalesContract::getContractVersion, maxVersion)
                .eq(SalesContract::getDeleted, 0);
        return this.getOne(queryWrapper);
    }

    /**
     * NC推送创建/编辑销售合同 参数校验相关逻辑
     */
    private Result<String> checkNcSaveOrUpdateParam(NcSalesContractAddOrUpdateParam param) {
        String salesContractId = param.getSalesContractId();
        if (StrUtil.isBlank(salesContractId)) {
            log.error("销售合同主键不能为空:{}", JSONUtil.toJsonStr(param));
            return Result.error("销售合同主键不能为空");
        }

        String contractCode = param.getContractCode();
        if (StrUtil.isBlank(contractCode)) {
            log.error("销售合同编号不能为空:{}", JSONUtil.toJsonStr(param));
            return Result.error("销售合同编号不能为空");
        }
        return Result.ok();
    }

    /**
     * nc修改销售合同,实际逻辑是创建一个新版本的销售合同
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> ncUpdate(NcSalesContractAddOrUpdateParam param) {
        log.info("nc修改销售合同参数:{}", JsonUtils.toJSONString(param));
        String contractCode = param.getContractCode();
        if (StrUtil.isBlank(contractCode)) {
            log.error("销售合同编号不能为空:{}", JSONUtil.toJsonStr(param));
            throw new BusinessException("NC销售合同编号不能为空");
        }
        String salesContractId = param.getSalesContractId();
        String idKey = Constant.SAVE_UPDATE_SALES_CONTRACT_LOCK_KEY + salesContractId;
        String contractCodeKey = Constant.SAVE_UPDATE_SALES_CONTRACT_LOCK_KEY + contractCode;
        log.info("获取锁:{},{}", idKey, contractCodeKey);
        try (ZLock idLock = redissonDistributedLock.lock(idKey); ZLock contractCodeLock = redissonDistributedLock.lock(idKey)) {
            //校验NC销售合同参数
            Result<String> checkResult = checkNcSaveOrUpdateParam(param);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }

            //查询同版本销售合同是否已经存在
            SalesContract salesContract = checkContractExistsAndGetSameVersion(param);
            if(salesContract!=null){
                return updateContract(param, salesContract);
            }

            return ncCreateNewVersionContract(param);
        } catch (Exception e) {
            log.error("nc修改销售合同异常", e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 更新销售合同
     * @param ncParam nc修改销售合同参数
     * @param sourceSalesContract 原销售合同
     */
    private Result<String> updateContract(NcSalesContractAddOrUpdateParam ncParam, SalesContract sourceSalesContract) {
        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setOperationType(SalesContractHistoryOperationTypeEnum.UPDATE.getType());


        SalesContractContext contractContext = new SalesContractContext();
        //处理基础信息内部匹配
        handleBaseInfo(contractContext,ncParam);

        //处理销售合同主表逻辑
        SalesContract tmsContract = updateSalesContract(sourceSalesContract, ncParam, contractContext,historyDTO);
        contractContext.setTmsContract(tmsContract);

        //设置合同条款
        handleContractTerms(true, sourceSalesContract.getId(), ncParam, historyDTO);

        //设置合同货物
        updateContractGoods(sourceSalesContract.getId(), ncParam, historyDTO, contractContext);

        //禁用其他的版本
        disableOtherVersionAndSyncGoodsAmount(sourceSalesContract);

        //处理内部匹配
        contractContext.setUpdate(true);
        handleInternalMatching(ncParam,tmsContract,contractContext);

        //保存历史记录
        historyDTO.setAfterSalesContract(getById(tmsContract.getId()));
        handleHistoryRecord(contractContext,ncParam,historyDTO);
        return Result.ok();
    }

    /**
     * 处理内部匹配
     * @param ncParam nc参数
     * @param tmsContract 销售合同主表
     * @param contractContext 基础信息匹配错误信息
     */
    private void handleInternalMatching(NcSalesContractAddOrUpdateParam ncParam,SalesContract tmsContract,SalesContractContext contractContext) {

        //关联物流协议
        boolean update = contractContext.isUpdate();
        String yearAgreement = ncParam.getYearAgreement();
        if (StringUtils.isNotBlank(yearAgreement)){
            Result<FrameAgreement> checkResult = checkCanRelate(ncParam);
            if(checkResult.isSuccess()){
                Result<String> result = Result.ok(checkResult.getMsg());
                FrameAgreement agreement = checkResult.getData();
                relateAgreement(tmsContract, agreement,contractContext);
                contractContext.setMatchAgreementResult(result);
            }else {
                Result<String> result = Result.error(checkResult.getMsg());
                contractContext.setMatchAgreementResult(result);
                if(update){
                    //在修改的场景下 在关联失败的场景下，需要取消当前版本号已有的关联
                    cancelExistsRelate(contractContext);
                }
            }
        }else if(update){
            log.warn("协议号为空,不进行关联操作");
            //在修改的场景下 nc推送协议号为空，需要取消当前版本号已有的关联
            cancelExistsRelate(contractContext);
        }



        //修改托盘客户
        String trayCustomerName = ncParam.getTrayCustomerName();
        if (StringUtils.isNotBlank(trayCustomerName)){
            Result<String> trayCustomerResult = matchTrayCustomer(ncParam, tmsContract);
            contractContext.setMatchTrayCustomerResult(trayCustomerResult);
        }
    }
    private  void cancelExistsRelate(SalesContractContext contractContext) {
        SalesContract tmsContract = contractContext.getTmsContract();
        Long agreementId = tmsContract.getFrameAgreementId();
        if (agreementId==null){
            return;
        }
        SysUser sysUser = LoginUserContextHolder.getUser();
        Long id = tmsContract.getId();
        //首先查询是否已经存在已经关联的子表
        LambdaQueryWrapper<FrameAgreementSub> subWrapper = new LambdaQueryWrapper<>();
        subWrapper.eq(FrameAgreementSub::getSaleContractId, id)
                .eq(FrameAgreementSub::getRelate, DicConstant.NumEnums.NUM_ONE.getIntegerValue())
                .eq(FrameAgreementSub::getDeleted, DicConstant.NumEnums.NUM_ZERO.getIntegerValue())
                .last("limit 1");
        FrameAgreementSub agreementSub = frameAgreementSubMapper.selectOne(subWrapper);
        if (agreementSub != null){
            LambdaUpdateWrapper<FrameAgreementSub> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(FrameAgreementSub::getId, agreementSub.getId())
                    .set(FrameAgreementSub::getSaleContractId, null)
                    .set(FrameAgreementSub::getRelate, DicConstant.NumEnums.NUM_ZERO.getIntegerValue())
                    .set(FrameAgreementSub::getModifyId, sysUser.getId())
                    .set(FrameAgreementSub::getModifyTime, LocalDateTime.now())
                    .set(FrameAgreementSub::getModifyName, sysUser.getId());
            frameAgreementSubMapper.update(null, updateWrapper);
        }
        cancelContractRelate(tmsContract);

        FrameAgreement lastFrameAgreement = frameAgreementService.getById(agreementId);
        //协议号变更 框架合同记录取消关联的日志
        FrameAgreementHistoryFieldVo beforeAgreement = new FrameAgreementHistoryFieldVo();
        beforeAgreement.setReContractCode(tmsContract.getCode());
        beforeAgreement.setCode(lastFrameAgreement.getCode());
        FrameAgreementHistoryFieldVo afterAgreement = new FrameAgreementHistoryFieldVo();
        afterAgreement.setReContractCode(Constant.DEFAULT_VALUE);
        afterAgreement.setCode(lastFrameAgreement.getCode());
        contractContext.setCancelBeforeAgreement(beforeAgreement);
        contractContext.setCancelAfterAgreement(afterAgreement);
    }
    /**
     * nc修改销售合同
     *
     * @param param
     * @return
     */
    private Result<String> ncCreateNewVersionContract(NcSalesContractAddOrUpdateParam param) {

        HistoryDTO historyDTO = new HistoryDTO();
        historyDTO.setOperationType(SalesContractHistoryOperationTypeEnum.CREATE.getType());

        SalesContractContext contractContext = new SalesContractContext();

        //处理基础信息内部匹配
        handleBaseInfo(contractContext,param);

        SalesContract tmsContract = new SalesContract();
        //处理销售合同主表逻辑
        handleSalesContract(tmsContract, param, contractContext);

        contractContext.setTmsContract(tmsContract);

        //设置合同条款
        handleContractTerms(false, tmsContract.getId(), param, historyDTO);

        //设置合同货物
        handleContractGoods(tmsContract.getId(), param, historyDTO, contractContext);

        //同一个合同同时只有一个启用的 禁用其他的版本
        //同步货量信息
        disableOtherVersionAndSyncGoodsAmount(tmsContract);

        //处理内部匹配
        handleInternalMatching(param,tmsContract,contractContext);

        //保存历史记录
        historyDTO.setAfterSalesContract(getById(tmsContract.getId()));

        handleHistoryRecord(contractContext,param,historyDTO);
        return Result.ok();
    }

    private void handleHistoryRecord(SalesContractContext contractContext, NcSalesContractAddOrUpdateParam param, HistoryDTO historyDTO) {
        SysUser sysUser = LoginUserContextHolder.getUser();
        SalesContract tmsContract = contractContext.getTmsContract();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if(status == TransactionSynchronization.STATUS_COMMITTED){
                    logisticContractHistoryService.saveHistoryRecord(sysUser.getName(), LocalDateTime.now(), historyDTO);

                    Result<String> matchAgreementResult = contractContext.getMatchAgreementResult();
                    if (matchAgreementResult!=null){
                        if (!matchAgreementResult.isSuccess()){
                            //框架合同内部匹配失败
                            InternalMatchingApiLogDto logDto = new InternalMatchingApiLogDto(TmsContractConstant.InternalMatchingType.SALES_CONTRACT,
                                    TmsContractConstant.InternalMatchingOperation.AGREEMENT,tmsContract.getCode(),param.getYearAgreement(),matchAgreementResult);
                            internalMatchingApiLogService.create(logDto);
                            log.info("框架合同内部匹配保存历史记录成功：{},{}",tmsContract.getCode(),param.getYearAgreement());
                        }else{
                            //记录取消关联日志
                            FrameAgreementHistoryFieldVo cancelBeforeAgreement = contractContext.getCancelBeforeAgreement();
                            FrameAgreementHistoryFieldVo cancelAfterAgreement = contractContext.getCancelAfterAgreement();
                            if(cancelAfterAgreement!=null){
                                cancelAfterAgreement.setReSource(0);
                                frameAgreementService.saveHistoryRecord(sysUser.getName(), tmsContract.getModifyTime(), cancelBeforeAgreement, cancelAfterAgreement, FrameAgreementHistoryOperationTypeEnum.CANCEL_RE_LOGIS_CONTRACT.getType());
                                log.info("框架合同取消关联保存历史记录成功：{},{}",tmsContract.getCode(),cancelAfterAgreement.getCode());
                            }
                            //记录框架合同关联日志
                            FrameAgreementHistoryFieldVo beforeAgreement = contractContext.getRelateBeforeAgreement();
                            FrameAgreementHistoryFieldVo afterAgreement = contractContext.getRelateAfterAgreement();
                            if(afterAgreement!=null){
                                afterAgreement.setReSource(0);
                                frameAgreementService.saveHistoryRecord(sysUser.getName(), tmsContract.getModifyTime(), beforeAgreement, afterAgreement, FrameAgreementHistoryOperationTypeEnum.RE_LOGIS_CONTRACT.getType());
                                log.info("框架合同关联保存历史记录成功：{},{}",tmsContract.getCode(),afterAgreement.getCode());
                            }
                        }
                    }
                    //修改托盘客户
                    Result<String> trayCustomerResult = contractContext.getMatchTrayCustomerResult();
                    contractContext.setMatchTrayCustomerResult(trayCustomerResult);
                    if (!trayCustomerResult.isSuccess()){
                        InternalMatchingApiLogDto logDto = new InternalMatchingApiLogDto(TmsContractConstant.InternalMatchingType.SALES_CONTRACT,
                                TmsContractConstant.InternalMatchingOperation.BASIC_INFO,tmsContract.getCode(),tmsContract.getTrayCustomerName(),trayCustomerResult);
                        internalMatchingApiLogService.create(logDto);
                    }

                    String baseInfoError = contractContext.getBaseInfoMatchErrorMsgBuilder().toString();
                    if (StringUtils.isNotBlank(baseInfoError)){
                        Result<String> baseMatchResult = Result.error(baseInfoError);
                        InternalMatchingApiLogDto logDto = new InternalMatchingApiLogDto(TmsContractConstant.InternalMatchingType.SALES_CONTRACT,
                                TmsContractConstant.InternalMatchingOperation.BASIC_INFO,tmsContract.getCode(),tmsContract.getCode(),baseMatchResult);
                        internalMatchingApiLogService.create(logDto);
                    }
                }
            }
        });
    }

    /**
     * 处理基础信息
     * @param contractContext
     * @param param
     */
    private void handleBaseInfo(SalesContractContext contractContext,NcSalesContractAddOrUpdateParam param){
        StringBuilder baseInfoMatchErrorMsgBuilder = contractContext.getBaseInfoMatchErrorMsgBuilder();
        //校验发运公司是否存在
        List<NcSalesContractGoodsAddParam> salesContractGoods = param.getSalesContractGoods();
        if (CollUtil.isNotEmpty(salesContractGoods)){
            Map<String,CompanyVo> shippingCompanyMap = getShippingCompany(salesContractGoods,baseInfoMatchErrorMsgBuilder);
            contractContext.setShippingCompanyMap(shippingCompanyMap);
        }

        //校验客户信息
        String customerUscc = param.getCustomerUscc();
        Result<CompanyVo> customerResult = getCompanyByUsccCode(customerUscc, "客户",baseInfoMatchErrorMsgBuilder);
        CompanyVo customer = null;
        if (customerResult.isSuccess()) {
            customer = customerResult.getData();
            contractContext.setCustomer(customer);
        }

        //校验产品线
        Result<ProductLineVo> productLineResult = getProductLineVo(param.getProductLineCode(),baseInfoMatchErrorMsgBuilder);
        ProductLineVo productLineVO = null;
        if (productLineResult.isSuccess()) {
            productLineVO = productLineResult.getData();
            contractContext.setMainProductLine(productLineVO);
        }

        //校验销售组织
        String salesOrganizationUscc = param.getSalesOrganizationUscc();
        Result<CompanyVo> salesOrgResult = getCompanyByUsccCode(salesOrganizationUscc, "销售组织",baseInfoMatchErrorMsgBuilder);
        CompanyVo salesOrg = null;
        if (salesOrgResult.isSuccess()) {
            salesOrg = salesOrgResult.getData();
            contractContext.setSalesOrg(salesOrg);
        }
    }

    /**
     * 获取货物发运公司信息
     * @param salesContractGoods nc销售合同货物
     * @param baseInfoMatchErrorMsgBuilder 基础信息匹配错误信息
     */
    private Map<String,CompanyVo> getShippingCompany(List<NcSalesContractGoodsAddParam> salesContractGoods,StringBuilder baseInfoMatchErrorMsgBuilder){
        Map<String,CompanyVo> usccCodeCompanyMap = new HashMap<>();
        //校验发运公司是否存在
        for (NcSalesContractGoodsAddParam salesContractGood : salesContractGoods) {
            String shippingCompanyUscc = salesContractGood.getShippingCompanyUscc();
            if (StringUtils.isBlank(shippingCompanyUscc)){
                continue;
            }
            if (usccCodeCompanyMap.containsKey(shippingCompanyUscc)){
                continue;
            }
            Result<CompanyVo> companyResult = getCompanyByUsccCode(shippingCompanyUscc, "发运公司",baseInfoMatchErrorMsgBuilder);
            if (!companyResult.isSuccess()){
                continue;
            }
            usccCodeCompanyMap.put(shippingCompanyUscc,companyResult.getData());
        }
        return usccCodeCompanyMap;
    }
    /**
     * 匹配产品线
     */
    private Result<String> matchProductLine(ProductLineVo productLineVO) {
        Integer status = productLineVO.getStatus();
        if (status == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(status)) {
            log.error("产品线未启用,{}", JsonUtils.toJSONString(productLineVO));
            return Result.error("产品线【"+productLineVO.getProductLineName()+"("+productLineVO.getProductLineCode()+")"+"】未启用");
        }
        return Result.ok();
    }

    /**
     * 匹配公司基础信息
     * @param companyVo
     * @return
     */
    private Result<String> matchBaseInfo(CompanyVo companyVo,String prefix) {
        //校验发运公司启用并认证
        Long enableStatus = companyVo.getEnableStatus();
        Long certificationState = companyVo.getCertificationState();
        if (enableStatus == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(enableStatus.intValue()) || certificationState == null || DicConstant.NumEnums.NUM_ZERO.getIntegerValue().equals(certificationState.intValue())) {
            log.error("{}公司未认证或者未认证,{}",prefix, JsonUtils.toJSONString(companyVo));
            return Result.error(prefix+"公司【"+companyVo.getName()+"("+companyVo.getUnifiedSocialCreditIdentifier()+")"+"】未启用或者未认证");
        }
        return Result.ok();
    }

    /**
     * 获取产品线信息
     */
    private Result<ProductLineVo> getProductLineVo(String productLineCode,StringBuilder baseInfoMatchErrorMsgBuilder) {
        if (StrUtil.isBlank(productLineCode)) {
            log.error("产品线为空");
            return Result.error("产品线编码为空");
        }
        log.info("根据产品线编码查询产品线详情开始:{}", productLineCode);
        Result<ProductLineVo> lineResult = thirdPartDataFeign.getProductLineByCode(productLineCode);
        log.info("根据产品线编码查询产品线详情结束:{}", JSONObject.toJSONString(lineResult));

        if (!lineResult.isSuccess()){
            baseInfoMatchErrorMsgBuilder.append("产品线【").append(productLineCode).append("】查询失败:").append(lineResult.getMsg()).append(";");
            return lineResult;
        }
        if (lineResult.getData() == null) {
            baseInfoMatchErrorMsgBuilder.append("产品线【").append(productLineCode).append("】不存在;");
            return Result.error("产品线不存在");
        }
        return lineResult;
    }

    /**
     * 根据社会统一信用代码查询公司信息
     */
    private Result<CompanyVo> getCompanyByUsccCode(String usccCode, String logPrefix,StringBuilder baseInfoMatchErrorMsgBuilder) {
        if (StrUtil.isBlank(usccCode)){
            log.info("{}社会统一信用代码为空", logPrefix);
            return Result.error(logPrefix+"社会统一信用代码为空");
        }
        log.info("根据统一社会信用代码查询{}公司信息开始，入参:{}", logPrefix, usccCode);
        Result<CompanyVo> companyResult = companyFeign.getCompanyByIdentifier(usccCode);
        log.info("根据统一社会信用代码查询{}公司信息结束:{}", logPrefix, JSONObject.toJSON(companyResult));
        if(!companyResult.isSuccess()){
            baseInfoMatchErrorMsgBuilder.append(logPrefix).append("(").append(usccCode).append(")").append("获取失败:").append(companyResult.getMsg()).append(";");
            return companyResult;
        }
        if (companyResult.getData() == null) {
            baseInfoMatchErrorMsgBuilder.append(logPrefix).append("(").append(usccCode).append(")").append("不存在").append(";");
            return Result.error(logPrefix + "不存在");
        }
        return companyResult;
    }

    @Override
    public SalesContractDetailVO getDetail(String id) {
        SalesContract salesContract = getById(id);
        if (salesContract == null) {
            throw new BusinessException("合同不存在");
        }
        SalesContractDetailVO detailVO = new SalesContractDetailVO();
        detailVO.setId(salesContract.getId());
        detailVO.setContractName(salesContract.getName());
        detailVO.setOrgId(salesContract.getSalesOrgId());
        detailVO.setOrgUscc(salesContract.getSalesOrgUscc());
        detailVO.setOrgName(salesContract.getSalesOrgName());
        detailVO.setContractCode(salesContract.getCode());
        detailVO.setContractTypeCode(salesContract.getContractType());
        detailVO.setContractTypeName(SalesContractTypeEnum.getByCode(salesContract.getContractType()));
        detailVO.setSignedTime(salesContract.getSignedTime());
        detailVO.setEffectiveTime(salesContract.getEffectiveTime());
        detailVO.setEndTime(salesContract.getEndTime());
        detailVO.setSalesman(salesContract.getSalesman());
        detailVO.setDepartment(salesContract.getDepartment());
        detailVO.setCustomerId(salesContract.getCustomerId());
        detailVO.setCustomerUscc(salesContract.getCustomerUscc());
        detailVO.setCustomerName(salesContract.getCustomerName());
        detailVO.setTrayCustomerId(salesContract.getTrayCustomerId());
        detailVO.setTrayCustomerUscc(salesContract.getNcTrayCustomerId());
        detailVO.setTrayCustomerName(salesContract.getTrayCustomerName());
        detailVO.setProductLineId(salesContract.getProductLineId());
        detailVO.setProductLineCode(salesContract.getProductLineCode());
        detailVO.setProductLineName(salesContract.getProductLineName());
        detailVO.setAgreementId(salesContract.getFrameAgreementId());
        Long agreementId = salesContract.getFrameAgreementId();
        if (agreementId!=null){
            detailVO.setAgreementCode(salesContract.getFrameAgreementCode());
        }else{
            detailVO.setAgreementCode(salesContract.getNcAgreementCode());
        }
        detailVO.setSaleTypeCode(salesContract.getSaleType());
        detailVO.setSaleTypeName(SaleTypeEnum.getByCode(salesContract.getSaleType()));
        detailVO.setPriceTypeCode(salesContract.getPriceType());
        detailVO.setPriceTypeName(PriceTypeEnum.getByCode(salesContract.getPriceType()));
        detailVO.setBearCostType(salesContract.getBearCostType());
        detailVO.setAgreementDate(salesContract.getAgreementDate());
        detailVO.setBusinessType(salesContract.getBusinessType());
        detailVO.setDeparturePlace(salesContract.getDeparturePlace());
        detailVO.setDetainedGoods(salesContract.getDetainedGoods());
        detailVO.setWholeMeasurement(salesContract.getWholeMeasurement());
        detailVO.setTotalNumber(salesContract.getTotalNumber());
        detailVO.setOceanCustomers(salesContract.getOceanCustomers());
        detailVO.setTotalPriceTax(NumberUtil.decimalToString(salesContract.getTotalPriceTax()));
        detailVO.setDiscountYear(salesContract.getDiscountYear());
        detailVO.setPoundNote(salesContract.getPoundNote());
        detailVO.setRemark(salesContract.getRemark());
        detailVO.setTwoFactoryTrade(salesContract.getTwoFactoryTrade());
        detailVO.setTwoFactoryTradeCode(salesContract.getTwoFactoryTradeCode());
        detailVO.setDisabled(salesContract.getDisabled());
        //查询条款信息
        LambdaQueryWrapper<SalesContractTerms> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SalesContractTerms::getSalesContractId, salesContract.getId())
                .eq(SalesContractTerms::getDeleted, false);
        List<SalesContractTerms> salesContractList = salesContractTermsMapper.selectList(wrapper);
        List<SalesContractTermsVO> salesContractTerms = new ArrayList<>();
        for (SalesContractTerms terms : salesContractList) {
            SalesContractTermsVO salesContractTermsVO = new SalesContractTermsVO();
            salesContractTermsVO.setId(terms.getId());
            salesContractTermsVO.setTermCode(terms.getTermCode());
            salesContractTermsVO.setTermName(terms.getTermName());
            salesContractTermsVO.setTermContent(terms.getTermContent());
            salesContractTerms.add(salesContractTermsVO);
        }
        detailVO.setSalesContractTerms(salesContractTerms);
        //查询货物信息
        LambdaQueryWrapper<SalesContractGoods> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.eq(SalesContractGoods::getSalesContractId, salesContract.getId())
                .eq(SalesContractGoods::getDeleted, false);
        List<SalesContractGoods> salesContractGoodsList = salesContractGoodsMapper.selectList(goodsWrapper);
        List<SalesContractGoodsVO> salesContractGoods = new ArrayList<>();
        for (SalesContractGoods goods : salesContractGoodsList) {
            SalesContractGoodsVO salesContractGoodsVO = new SalesContractGoodsVO();
            salesContractGoodsVO.setId(goods.getId());
            salesContractGoodsVO.setLineNumber(goods.getLineNumber());
            salesContractGoodsVO.setContractGoodsId(goods.getContractGoodsId());
            salesContractGoodsVO.setMaterialClassificationName(goods.getMaterialClassification());
            salesContractGoodsVO.setReferenceMaterial(goods.getReferenceMaterial());
            salesContractGoodsVO.setMaterial(goods.getMaterial());
            salesContractGoodsVO.setTexture(goods.getTexture());
            salesContractGoodsVO.setProductLineId(goods.getProductLineId());
            salesContractGoodsVO.setProductLineCode(goods.getProductLineCode());
            salesContractGoodsVO.setProductLineName(goods.getProductLineName());
            salesContractGoodsVO.setQualityGrade(goods.getQualityGrade());
            salesContractGoodsVO.setQuantity(goods.getQuantity());
            salesContractGoodsVO.setMainQuantity(goods.getMainQuantity());
            salesContractGoodsVO.setMainUnit(goods.getMainUnit());
            salesContractGoodsVO.setConversionRate(goods.getConversionRate());
            salesContractGoodsVO.setBasePrice(NumberUtil.decimalToString(goods.getBasePrice()));
            salesContractGoodsVO.setReceivingLocation(goods.getReceivingLocation());
            salesContractGoodsVO.setWharf(goods.getWharf());
            salesContractGoodsVO.setMainTaxUnitPrice(NumberUtil.decimalToString(goods.getMainTaxUnitPrice()));
            salesContractGoodsVO.setTotalPriceTax(NumberUtil.decimalToString(goods.getTotalPriceTax()));
            salesContractGoodsVO.setReceivingCountry(goods.getReceivingCountry());
            salesContractGoodsVO.setTaxCode(goods.getTaxCode());
            salesContractGoodsVO.setPriceComposition(goods.getPriceComposition());
            salesContractGoodsVO.setRemark(goods.getRemark());
            salesContractGoodsVO.setMaterialSpecification(goods.getMaterialSpecification());
            salesContractGoodsVO.setContractCode(salesContract.getCode());
            salesContractGoodsVO.setShippingCompanyId(goods.getShippingCompanyId());
            salesContractGoodsVO.setShippingCompanyName(goods.getShippingCompanyName());
            salesContractGoodsVO.setShippingCompanyUscc(goods.getShippingCompanyUscc());
            salesContractGoods.add(salesContractGoodsVO);
        }
        detailVO.setSalesContractGoods(salesContractGoods);
        return detailVO;
    }

    @Override
    public ApiPageResult<SalesContractListVO> querySalesContractPageList(SalesContractListParam param) {
        //使用单号精确查询,忽略掉时间参数
        String contractNameCode = param.getContractNameCode();
        if (StringUtils.isNotBlank(contractNameCode)) {
            param.setCreateTimeStart(null);
            param.setCreateTimeEnd(null);
        }
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<SalesContractListVO> salesContractList = salesContractMapper.querySalesContractPageList(param);
        for (SalesContractListVO salesContract : salesContractList) {
            //销售合同类型
            Integer contractType = salesContract.getContractType();
            salesContract.setContractTypeName(SalesContractTypeEnum.getByCode(contractType));

            //价格类型
            Integer priceType = salesContract.getPriceType();
            salesContract.setPriceTypeName(PriceTypeEnum.getByCode(priceType));

            //版本号
            Integer systemSource = salesContract.getSystemSource();
            String contractVersion = salesContract.getContractVersion();
            if (StringUtils.isNotBlank(contractVersion) && systemSource.equals(SystemSourceEnum.NC.getCode())) {
                salesContract.setContractVersion(contractVersion);
            } else {
                salesContract.setContractVersion("—");
            }

            //业务员 显示为 业务员-部门
            String salesmanField = "";
            String salesman = salesContract.getSalesman();
            String department = salesContract.getDepartment();
            if (StringUtils.isNotBlank(salesman)) {
                salesmanField = salesman;
                if (StringUtils.isNotBlank(department)) {
                    salesmanField = department + "—" + salesmanField;
                }
            } else {
                salesmanField = "—";
            }
            salesContract.setSalesman(salesmanField);

            String totalPriceTax = salesContract.getTotalPriceTax();
            salesContract.setTotalPriceTax(NumberUtil.decimalToString(totalPriceTax));
        }
        //从货物表统计拉运量
        List<Long> contractIds = salesContractList.stream().map(SalesContractListVO::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(contractIds)) {
            List<GoodsQuantityDTO> goodsQuantityList = salesContractGoodsService.sumGoodsQuantityByContractId(contractIds);
            //转换为map
            Map<Long, GoodsQuantityDTO> goodsQuantityMap = goodsQuantityList.stream().collect(Collectors.toMap(GoodsQuantityDTO::getContractId, v -> v));
            for (SalesContractListVO SalesContractListVO : salesContractList) {
                GoodsQuantityDTO goodsQuantityDTO = goodsQuantityMap.get(SalesContractListVO.getId());
                if (goodsQuantityDTO != null) {
                    SalesContractListVO.setMainOrdersQuantity(NumberUtil.decimalToString(goodsQuantityDTO.getMainOrdersQuantity()));
                }else{
                    SalesContractListVO.setMainOrdersQuantity("—");
                }
            }
        }
        PageInfo<SalesContractListVO> pageInfo = new PageInfo<>(salesContractList);
        ApiPageResult<SalesContractListVO> resultApiPageResult = ApiPageResult.<SalesContractListVO>builder()
                .list(salesContractList)
                .currentPage(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .totalPage(pageInfo.getPages())
                .total((int) pageInfo.getTotal())
                .build();
        return resultApiPageResult;
    }


    /**
     * 修改物流合同状态
     * disabled 0启用 1禁用
     */
    private void updateState(UpdateStatusParam  param) {
        Boolean disabled = param.getDisabled();
        SalesContract salesContract = param.getSalesContract();
        if (salesContract==null) {
            log.error("修改物流合同状态参数为空");
            return;
        }
        Long id = salesContract.getId();
        SysUser user = LoginUserContextHolder.getUser();
        LambdaUpdateWrapper<SalesContract> update = new LambdaUpdateWrapper<>();
        update.set(SalesContract::getDisabled, disabled)
                .eq(SalesContract::getId, id)
                .set(SalesContract::getModifyId, user.getId())
                .set(SalesContract::getModifyTime, LocalDateTime.now())
                .set(SalesContract::getModifyName, user.getUsername());
        this.update(update);

        //记录历史记录

        HistoryDTO historyDTO = new HistoryDTO();
        SalesContractHistorySaveDTO after = new SalesContractHistorySaveDTO();
        after.setCode(salesContract.getCode());
        after.setName(salesContract.getName());
        after.setDisabled(disabled);
        SalesContractHistorySaveDTO before = new SalesContractHistorySaveDTO();
        before.setCode(salesContract.getCode());
        before.setName(salesContract.getName());
        before.setDisabled(!disabled);
        historyDTO.setAfter(after);
        historyDTO.setBefore(before);
        historyDTO.setOperationType(SalesContractHistoryOperationTypeEnum.UPDATE_STATE.getType());
        logisticContractHistoryService.saveHistoryRecord(user.getUsername(), LocalDateTime.now(), historyDTO);

        //同步业务来源
        Integer busiSource = salesContract.getBusiSource();
        if (DicConstant.LOGIS_CONTRACT_AGREEMENT_BUSI_SOURCE.NC.equals(busiSource)||busiSource==null){
            List<SalesContractGoods> contractGoods = getContractGoods(salesContract.getId());
            String shippingCompanyId = null;
            if (CollUtil.isNotEmpty(contractGoods)){
                shippingCompanyId = contractGoods.get(0).getNcShippingCompany();
            }
            //说明获取基地来源失败,在合同数据变更时需要重新尝试重新获取,获取成功后更新相关数据
            getAndSynBusiSource(salesContract.getSalesOrgName(),shippingCompanyId);
        }
    }

    /**
     * nc变更合同状态
     * 禁用-不需要校验基础信息直接禁用即可
     * 启用-需要校验基础信息存在并且是启用状态
     */
    @Override
    public Result<String> ncUpdateState(NcUpdateStateParam param) {
        String salesContractId = param.getSalesContractId();
        if(StringUtils.isBlank(salesContractId)){
            return Result.error("销售合同id不能为空");
        }

        Integer contractStatus = param.getContractStatus();
        if(contractStatus == null){
            return Result.error("状态不能为空");
        }

        Boolean targetDisabled = contractStatus == 1;
        //根据nc销售合同id查询最新版本的合同
        SalesContract salesContract = getLatestSalesContractByContractId(salesContractId);
        if (salesContract == null){
            log.error("该销售合同:{}不存在", salesContractId);
            return Result.error("该销售合同不存在");
        }

        Boolean disabled = salesContract.getDisabled();
        if(disabled.equals(targetDisabled)){
            log.warn("该销售合同:{}已处于{}状态,无需变更状态", salesContractId, targetDisabled ? "禁用" : "启用");
            return Result.ok();
        }

        UpdateStatusParam updateStatusParam = new UpdateStatusParam();
        updateStatusParam.setDisabled(targetDisabled);
        updateStatusParam.setSalesContract(salesContract);

        updateState(updateStatusParam);
        return Result.ok();
    }

    /**
     * 禁用其他已经启用的版本
     * 同一个合同主键下只有一个可以启用的销售合同
     */
    private void disableOtherVersionAndSyncGoodsAmount(SalesContract contract) {
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getSalesContractId, contract.getSalesContractId())
                .ne(SalesContract::getId, contract.getId())
                .eq(SalesContract::getDeleted, false);
        List<SalesContract> contractList = salesContractMapper.selectList(queryWrapper);
        List<Long> disableList = new ArrayList<>();
        List<Long> syncAmountList = new ArrayList<>();
        BigDecimal totalNumber = contract.getTotalNumber()==null? BigDecimal.ZERO : contract.getTotalNumber();
        for (SalesContract salesContract : contractList) {
            if (!salesContract.getDisabled()) {
                disableList.add(salesContract.getId());
            }
            BigDecimal contractTotalNumber = salesContract.getTotalNumber() == null? BigDecimal.ZERO : salesContract.getTotalNumber();
            if (totalNumber.compareTo(contractTotalNumber) != 0){
                syncAmountList.add(salesContract.getId());
            }
        }
        SysUser sysUser = LoginUserContextHolder.getUser();
        if (CollUtil.isNotEmpty(disableList)) {
            //禁用其他已经启用的版本
            LambdaUpdateWrapper<SalesContract> update = new LambdaUpdateWrapper<>();
            update.set(SalesContract::getDisabled, true)
                    .in(SalesContract::getId, disableList)
                    .set(SalesContract::getModifyId, sysUser.getId())
                    .set(SalesContract::getModifyTime, LocalDateTime.now())
                    .set(SalesContract::getModifyName, sysUser.getUsername());
            this.update(update);
        }

        if (CollUtil.isNotEmpty(syncAmountList)) {
            //同步货物总量
            LambdaUpdateWrapper<SalesContract> update = new LambdaUpdateWrapper<>();
            update.set(SalesContract::getTotalNumber, contract.getTotalNumber())
                    .in(SalesContract::getId, syncAmountList)
                    .set(SalesContract::getModifyId, sysUser.getId())
                    .set(SalesContract::getModifyTime, LocalDateTime.now())
                    .set(SalesContract::getModifyName, sysUser.getUsername());
            this.update(update);
        }
    }
    /**
     * 根据nc销售合同id查询最新版本号的物流合同信息
     *
     * @param salesContractId nc销售合同ID
     */
    private SalesContract getLatestSalesContractByContractId(String salesContractId) {
        LambdaQueryWrapper<SalesContract> contractQueryWrapper = new LambdaQueryWrapper<>();
        contractQueryWrapper.in(SalesContract::getSalesContractId, salesContractId)
                .eq(SalesContract::getDeleted, false)
                .last("order by disabled,contract_version desc,create_time desc limit 1");
        return salesContractMapper.selectOne(contractQueryWrapper);
    }

    /**
     * 根据合同id查询货物信息
     */
    @Override
    public List<SalesContractGoodsVO> getSalesContractGoods(String contractId) {
        LambdaQueryWrapper<SalesContractGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContractGoods::getSalesContractId, contractId)
                .eq(SalesContractGoods::getDeleted, false);
        List<SalesContractGoods> salesContractGoods = salesContractGoodsMapper.selectList(queryWrapper);
        List<SalesContractGoodsVO> contractGoodsList = new ArrayList<>();
        for (SalesContractGoods contractGoods : salesContractGoods) {
            SalesContractGoodsVO contractGoodsVO = new SalesContractGoodsVO();
            BeanUtils.copyProperties(contractGoods, contractGoodsVO);
            contractGoodsVO.setMaterialClassificationName(contractGoods.getMaterialClassification());
            contractGoodsList.add(contractGoodsVO);
        }
        return contractGoodsList;
    }

    @Override
    public SalesContractDetailVO getContractInfo(String contractCode) {
        //按照启用 最新版本 查询
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getCode, contractCode)
                .eq(SalesContract::getDeleted, false)
                .last("order by disabled,contract_version desc,create_time desc limit 1");
        SalesContract salesContract = salesContractMapper.selectOne(queryWrapper);
        if (salesContract == null) {
            log.error("销售合同不存在:{}", contractCode);
            throw new BusinessException("销售合同不存在");
        }
        Long id = salesContract.getId();
        return getDetail(String.valueOf(id));
    }

    @Override
    public Boolean querySalesContractBySalesContractIdAndVersion(String salesContractId,Integer version) {
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getSalesContractId, salesContractId)
                .eq(version!=null,SalesContract::getContractVersion, version)
                .eq(SalesContract::getDeleted, false);
        SalesContract contract = salesContractMapper.selectOne(queryWrapper);
        return contract!= null;
    }

    /**
     * 根据nc销售合同id获取销售合同信息
     * @param salesContractId nc销售合同ID
     */
    @Override
    public SalesContract getSalesContractBySalesContractId(String salesContractId) {
        LambdaQueryWrapper<SalesContract> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SalesContract::getSalesContractId, salesContractId)
                .eq(SalesContract::getDeleted, false)
                .last("order by disabled,contract_version desc,create_time desc limit 1");
        return salesContractMapper.selectOne(queryWrapper);
    }
}
