package com.xtm.contract.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xtm.common.log.model.dto.BusinessChangeLogDto;
import com.xtm.common.log.model.dto.ChangeLogDto;
import com.xtm.common.log.service.IBusinessHitoryService;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.enums.BearCostEnum;
import com.xtm.contract.enums.PriceTypeEnum;
import com.xtm.contract.enums.SaleTypeEnum;
import com.xtm.contract.enums.SalesContractHistoryOperationTypeEnum;
import com.xtm.contract.enums.SalesContractTypeEnum;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.dto.ChangesDto;
import com.xtm.contract.model.dto.HistoryDTO;
import com.xtm.contract.model.dto.contract.SalesContractHistorySaveDTO;
import com.xtm.contract.model.dto.contract.SalesContractFixedMetadataDTO;
import com.xtm.contract.model.dto.contract.SalesContractHistoryDTO;
import com.xtm.contract.model.enums.FrameAgreementBusiSourceEnum;
import com.xtm.contract.model.enums.FrameAgreementHistoryOperationTypeEnum;
import com.xtm.contract.model.enums.FrameAgreementSystemSourceEnum;
import com.xtm.contract.model.param.SalesContractHistoryListParam;
import com.xtm.contract.model.vo.SalesContractHistoryListVO;
import com.xtm.contract.model.vo.SalesContractHistoryDetailVO;
import com.xtm.contract.service.SalesContractHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SalesContractHistoryServiceImpl implements SalesContractHistoryService {

    @Autowired
    private IBusinessHitoryService businessHitoryService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ApiPageResult<SalesContractHistoryListVO> getHistoryList(SalesContractHistoryListParam param) {
        ApiPageResult result = new ApiPageResult();
        // 历史记录默认显示为空
        if(StrUtil.isEmpty(param.getOperator()) && StrUtil.isEmpty(param.getContractNameCode()) && param.getDisabled() == null){
            return result;
        }
        // 动态构建查询条件
        Query query = this.buildQuery(param);
        // 总条数
        long total = mongoTemplate.count(query, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);

        // 设置分页参数
        long offset = (param.getPageNum() - 1) * param.getPageSize();
        // 分页
        query.skip(offset).limit(param.getPageSize().intValue());

        // 执行查询
        List<SalesContractHistoryDTO> datas = mongoTemplate.find(query, SalesContractHistoryDTO.class, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        List<SalesContractHistoryListVO> responseVoList = new ArrayList<>();

        //转换数据
        if (CollectionUtils.isNotEmpty(datas)) {
            for (int i = 0; i < datas.size(); i++) {
                SalesContractHistoryDTO historyDto = datas.get(i);
                SalesContractFixedMetadataDTO fixedMetadataVo = historyDto.getFixedMetadata();
                SalesContractHistoryListVO responseVo = new SalesContractHistoryListVO();
                responseVo.setId(historyDto.getId());

                if (fixedMetadataVo != null) {
                    responseVo.setContractName(fixedMetadataVo.getContractName());
                    responseVo.setContractCode(fixedMetadataVo.getContractCode());

                    if (historyDto.getOperation_type().equals(SalesContractHistoryOperationTypeEnum.UPDATE_STATE.getType())){
                        // 修改前后不同,显示修改后的状态  停用标记 0: 未停用 1: 已停用
                        responseVo.setDisabled(fixedMetadataVo.getAfterDisabled() ? "1" : "0");
                        responseVo.setAdjustContent("");
                    } else {
                        // 修改前后相同,说明没有修改合同状态
                        responseVo.setDisabled("");
                        responseVo.setAdjustContent("1");
                    }
                }

                responseVo.setOperateTime(DateUtil.format(historyDto.getOperation_time(), "yyyy-MM-dd HH:mm:ss"));//修改操作时间;
                responseVo.setOperator(historyDto.getOperator());//操作人姓名
                responseVo.setOperateType(FrameAgreementHistoryOperationTypeEnum.getOperationDesc(historyDto.getOperation_type()));//操作类型;

                responseVoList.add(responseVo);
            }
        }

        // 列表排序为操作时间的倒叙时间排列
        responseVoList = responseVoList.stream().sorted(Comparator.comparing(SalesContractHistoryListVO::getOperateTime).reversed()).collect(Collectors.toList());
        result.setList(responseVoList);
        result.setTotal((int) total);
        result.setPageSize(param.getPageSize());
        result.setCurrentPage(param.getPageNum());

        return result;
    }
    //组装查询参数;
    private Query buildQuery(SalesContractHistoryListParam param) {

        // 动态构建查询条件
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("business_type").is(Constant.TMS_CONTRACT_SALES_CONTRACT_MODULE);

        //单据号搜索(合同协议名称/合同协议编号)
        if (StrUtil.isNotEmpty(param.getContractNameCode())) {
            Criteria criteria1 = Criteria.where("fixed_metadata.contractName").is(param.getContractNameCode());
            Criteria criteria2 = Criteria.where("fixed_metadata.contractCode").is(param.getContractNameCode());
            Criteria combinedCriteria = new Criteria().orOperator(criteria1, criteria2);
            criteria.andOperator(combinedCriteria);
        }

        //操作人
        if (StrUtil.isNotEmpty(param.getOperator())) {
            criteria.and("operator").is(param.getOperator());
        }

        //合同状态 停用标记 0: 未停用 1: 已停用
        if (param.getDisabled() != null) {
            criteria.and("fixed_metadata.afterDisabled").is(param.getDisabled());
        }

        // 设置条件及排序规则
        query.addCriteria(criteria).with(Sort.by(Sort.Direction.DESC, "operation_time"));

        return query;
    }
    /**
     * 记录操作历史记录
     */
    @Async("asyncExecutor")
    @Override
    public void saveHistoryRecord(String sysUserName, LocalDateTime now , HistoryDTO historyDTO) {
        log.info("保存历史记录开始");
        SalesContractHistorySaveDTO before = historyDTO.getBefore();
        SalesContractHistorySaveDTO after = historyDTO.getAfter();
        String operationType = historyDTO.getOperationType();
        // 记录操作历史记录
        BusinessChangeLogDto businessChangeLogDto = new BusinessChangeLogDto();

        businessChangeLogDto.setCollectionName(Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        List<ChangeLogDto> changeLogDtos = new ArrayList<>();
        ChangeLogDto changeLogDto = new ChangeLogDto();
        SalesContractFixedMetadataDTO fixedMetadataVo = new SalesContractFixedMetadataDTO();//业务固定属性;
        fixedMetadataVo.setContractName(after.getName());
        fixedMetadataVo.setContractCode(after.getCode());

        if(operationType.equals(FrameAgreementHistoryOperationTypeEnum.CREATE.getType())){
            changeLogDto.setAfter(JSONUtil.toJsonStr(after));

        } else if (operationType.equals(SalesContractHistoryOperationTypeEnum.UPDATE.getType())||
                operationType.equals(SalesContractHistoryOperationTypeEnum.RE_FRAME_CONTRACT.getType())||
                operationType.equals(SalesContractHistoryOperationTypeEnum.CANCEL_RE_FRAME_CONTRACT.getType())
        ){
            changeLogDto.setAfter(JSONUtil.toJsonStr(after));
            changeLogDto.setBefore(JSONUtil.toJsonStr(before));

        } else if (operationType.equals(SalesContractHistoryOperationTypeEnum.UPDATE_STATE.getType())){
            fixedMetadataVo.setBeforeDisabled(before.getDisabled());
            fixedMetadataVo.setAfterDisabled(after.getDisabled());
        }

        changeLogDto.setId(String.valueOf(IdWorker.getId()));
        changeLogDto.setOperator(sysUserName);
        changeLogDto.setOperationType(operationType);
        changeLogDto.setBusinessModule(Constant.TMS_CONTRACT_SALES_CONTRACT_MODULE);
        changeLogDto.setOperationTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        changeLogDto.setFixedMetadata(fixedMetadataVo);

        changeLogDtos.add(changeLogDto);
        businessChangeLogDto.setChangeLogDtos(changeLogDtos);
        try {
            businessHitoryService.save(businessChangeLogDto);
            log.info("保存历史记录成功：{},{}",after.getCode(),operationType);
        } catch (Exception e) {
            log.error("保存历史记录失败,参数:", e);
        }

    }

    @Override
    public SalesContractHistoryDetailVO getHistoryDetailById(String recordId) {
        SalesContractHistoryDetailVO detailResponse = new SalesContractHistoryDetailVO();
        SalesContractHistoryDTO businessHistory = mongoTemplate.findById(recordId, SalesContractHistoryDTO.class, Constant.BUSINESS_HISTORY_MONGO_COLLECTION_NAME);
        if (businessHistory != null && businessHistory.getChanges() != null) {
            SalesContractFixedMetadataDTO fixedMetadataVo = businessHistory.getFixedMetadata();
            ChangesDto changes = businessHistory.getChanges();//变更前后的内容;
            String beforeJson = changes.getBefore();
            if(StrUtil.isNotEmpty(beforeJson)){
                JSONObject beforeJsonObject = JSONObject.parseObject(beforeJson, JSONObject.class);
                beforeJson = changeDto(beforeJsonObject);
            }
            String afterJson = changes.getAfter();
            if(StrUtil.isNotEmpty(afterJson)){
                JSONObject afterJsonObject = JSONObject.parseObject(afterJson, JSONObject.class);
                afterJson = changeDto(afterJsonObject);
            }
            detailResponse.setContractCode(fixedMetadataVo.getContractCode());
            detailResponse.setBeforeContent(beforeJson);
            detailResponse.setAfterContent(afterJson);
        }
        return detailResponse;
    }
    /**
     * 字符转换
     * @param jsonObject
     */
    private String changeDto(JSONObject jsonObject) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        if (jsonObject.containsKey("bearCostType")) {
            Integer bearCostType = (Integer) jsonObject.get("bearCostType");
            jsonObject.put("bearCostType", BearCostEnum.getByCode(bearCostType));
        }

        if (jsonObject.containsKey("contractType")) {
            Integer contractType = (Integer) jsonObject.get("contractType");
            jsonObject.put("contractType", SalesContractTypeEnum.getByCode(contractType));
        }


        if (jsonObject.containsKey("saleType")) {
            Integer saleType = (Integer) jsonObject.get("saleType");
            jsonObject.put("saleType", SaleTypeEnum.getByCode(saleType));
        }

        if (jsonObject.containsKey("priceType")) {
            Integer priceType = (Integer) jsonObject.get("priceType");
            jsonObject.put("priceType", PriceTypeEnum.getByCode(priceType));
        }

        if (jsonObject.containsKey("disabled")) {
            Boolean disabled = (Boolean) jsonObject.get("disabled");
            if (disabled != null){
                jsonObject.put("disabled", disabled?"禁用":"启用");
            }

        }
        if (jsonObject.containsKey("detainedGoods")) {
            Integer detainedGoods = (Integer) jsonObject.get("detainedGoods");
            if(detainedGoods!=null){
                jsonObject.put("detainedGoods", detainedGoods.equals(0) ? "否" : "是");
            }
        }
        if (jsonObject.containsKey("wholeMeasurement")) {
            Integer wholeMeasurement = (Integer) jsonObject.get("wholeMeasurement");
            if (wholeMeasurement != null){
                jsonObject.put("wholeMeasurement", wholeMeasurement.equals(0) ? "否" : "是");
            }
        }
        if (jsonObject.containsKey("oceanCustomers")) {
            Integer oceanCustomers = (Integer) jsonObject.get("oceanCustomers");
            if (oceanCustomers != null){
                jsonObject.put("oceanCustomers", oceanCustomers.equals(0) ? "否" : "是");
            }
        }
        if (jsonObject.containsKey("twoFactoryTrade")) {
            Integer twoFactoryTrade = (Integer) jsonObject.get("twoFactoryTrade");
            if (twoFactoryTrade != null){
                jsonObject.put("twoFactoryTrade", twoFactoryTrade.equals(0) ? "否" : "是");
            }
        }
        if (jsonObject.containsKey("busiSource")) {
            Integer busiSource = (Integer) jsonObject.get("busiSource");
            if (busiSource!=null){
                jsonObject.put("busiSource", FrameAgreementBusiSourceEnum.getOperationDesc(busiSource));
            }
        }
        if (jsonObject.containsKey("systemSource")) {
            Integer systemSource = (Integer) jsonObject.get("systemSource");
            if (systemSource!=null){
                jsonObject.put("systemSource", FrameAgreementSystemSourceEnum.getOperationDesc(systemSource));
            }

        }
        if (jsonObject.containsKey("signedTime")){
            Long signedTime = (Long)jsonObject.get("signedTime");
            if (signedTime != null){
                jsonObject.put("signedTime", formatter.format(Instant.ofEpochMilli(signedTime)));
            }
        }
        if (jsonObject.containsKey("effectiveTime")){
            Long effectiveTime = (Long) jsonObject.get("effectiveTime");
            if (effectiveTime != null){
                jsonObject.put("effectiveTime", formatter.format(Instant.ofEpochMilli(effectiveTime)));
            }
        }
        if (jsonObject.containsKey("endTime")){
            Long endTime = (Long) jsonObject.get("endTime");
            if(endTime!=null){
                jsonObject.put("endTime", formatter.format(Instant.ofEpochMilli(endTime)));
            }
        }
        if (jsonObject.containsKey("createTime")){
            Long createTime = (Long) jsonObject.get("createTime");
            if (createTime != null){
                jsonObject.put("createTime", formatter.format(Instant.ofEpochMilli(createTime)));
            }
        }
        if (jsonObject.containsKey("modifyTime")){
            Long modifyTime = (Long) jsonObject.get("modifyTime");
            if (modifyTime != null){
                jsonObject.put("modifyTime", formatter.format(Instant.ofEpochMilli(modifyTime)));
            }
        }

        if (jsonObject.containsKey("discountYear")){
            DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault());
            Long discountYear = (Long) jsonObject.get("discountYear");
            if (discountYear != null){
                jsonObject.put("discountYear", localDateFormatter.format(Instant.ofEpochMilli(discountYear)));
            }
        }

        if (jsonObject.containsKey("agreementDate")){
            DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    .withZone(ZoneId.systemDefault());
            Long agreementDate = (Long) jsonObject.get("agreementDate");
            if (agreementDate != null){
                jsonObject.put("agreementDate", localDateFormatter.format(Instant.ofEpochMilli(agreementDate)));
            }
        }

        return JSONObject.toJSONString(jsonObject);
    }
}
