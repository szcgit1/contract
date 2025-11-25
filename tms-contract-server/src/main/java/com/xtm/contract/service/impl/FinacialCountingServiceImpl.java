package com.xtm.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.SysUser;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.utils.DateUtil;
import com.xtm.contract.utils.StringUtils;
import com.xtm.contract.constant.ContractConstant;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.FddAuthMonthCost;
import com.xtm.contract.model.domain.FddContractDayCost;
import com.xtm.contract.model.domain.FddCostDetail;
import com.xtm.contract.model.query.finania.FddCostDetailReq;
import com.xtm.contract.model.vo.finance.FddAuthMonthCostVo;
import com.xtm.contract.model.vo.finance.FddAuthMonthExportVo;
import com.xtm.contract.model.vo.finance.FddContractDayCostVo;
import com.xtm.contract.model.vo.finance.FddContractDayExportVo;
import com.xtm.contract.model.vo.finance.FddCostDetailExportVo;
import com.xtm.contract.model.vo.finance.FddCostDetailVo;
import com.xtm.contract.service.FinacialCountingService;
import com.xtm.common.model.report.ExportRecord;
import com.xtm.report.common.template.ReportTemplate;
import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class FinacialCountingServiceImpl implements FinacialCountingService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ReportTemplate reportTemplate;

    @Override
    public ApiPageResult<FddCostDetailVo> page(FddCostDetailReq param) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getUserName())) {
            query.addCriteria(Criteria.where("userName").regex(param.getUserName(), "i")); // 使用正则表达式进行模糊匹配，"i"表示不区分大小写
        }
        if (StringUtils.isNotEmpty(param.getStartTime())  && StringUtils.isNotEmpty(param.getEndTime())) {
            long start = DateUtil.stringToDate(param.getStartTime()).getTime();
            long end = DateUtil.stringToDate(param.getEndTime()).getTime();
            query.addCriteria(Criteria.where("callTime").gte(start).lte(end));
        }
        if (StringUtils.isNotEmpty(param.getPhoneNum())) {
            query.addCriteria(Criteria.where("phoneNum").is(param.getPhoneNum()));
        }
        // 分页
        long total = mongoTemplate.count(query, FddCostDetail.class);
        Pageable pageable = PageRequest.of(param.getPageNum() -1, param.getPageSize(), Sort.by(Sort.Direction.fromString("DESC"), "callTime"));
        query.with(pageable);
        List<FddCostDetail> fddCostDetails = mongoTemplate.find(query, FddCostDetail.class);
        List<FddCostDetailVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(fddCostDetails)){
            for (FddCostDetail cost : fddCostDetails) {
                FddCostDetailVo vo = new FddCostDetailVo();
                BeanUtils.copyProperties(cost,vo);
                if(ContractConstant.costKeyword.auth.equals(vo.getType())) {
                    vo.setSuccess(cost.isSuccess() ? ContractConstant.AuthResultType.success : ContractConstant.AuthResultType.fail);
                }else {
                    vo.setSuccess("——");
                }
                vo.setCallTime(DateUtil.day2String(new Date(cost.getCallTime())));
                list.add(vo);
            }
        }
        ApiPageResult<FddCostDetailVo> resultApiPageResult = ApiPageResult.<FddCostDetailVo>builder()
                .currentPage(param.getPageNum())
                .pageSize(param.getPageSize())
                .totalPage((int)Math.ceil((double) total / param.getPageSize()))
                .total((int)total)
                .build();
        resultApiPageResult.setList(list);
        return resultApiPageResult;
    }

    @Override
    public void fddDetailExport(FddCostDetailReq param) {
        ExportRecord dto = buildExportRecord(Constant.EXPORT_TYPE.FDD_DETAIL, "法大大详情");
        //组装查询条件
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getUserName())) {
            query.addCriteria(Criteria.where("userName").regex(param.getUserName(), "i")); // 使用正则表达式进行模糊匹配，"i"表示不区分大小写
        }
        if (StringUtils.isNotEmpty(param.getStartTime())  && StringUtils.isNotEmpty(param.getEndTime())) {
            long start = DateUtil.stringToDate(param.getStartTime()).getTime();
            long end = DateUtil.stringToDate(param.getEndTime()).getTime();
            query.addCriteria(Criteria.where("callTime").gte(start).lte(end));
        }
        if (StringUtils.isNotEmpty(param.getPhoneNum())) {
            query.addCriteria(Criteria.where("phoneNum").is(param.getPhoneNum()));
        }
        query.with(Sort.by(Sort.Direction.fromString("DESC"), "callTime"));
        //执行导出
        mongoStreamExport(dto, query, FddCostDetail.class, FddCostDetailExportVo.class, fdd-> {
            FddCostDetailExportVo vo = new FddCostDetailExportVo();
            BeanUtils.copyProperties(fdd,vo);
            vo.setCallTime(new Date(fdd.getCallTime()));
            if(ContractConstant.costKeyword.auth.equals(fdd.getType())) {
                vo.setSuccess(fdd.isSuccess() ? ContractConstant.AuthResultType.success : ContractConstant.AuthResultType.fail);
            }else {
                vo.setSuccess("——");
            }
            return vo;
        });
    }

    @Override
    public ApiPageResult<FddAuthMonthCostVo> authPage(FddCostDetailReq param) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getPhoneNum())) {
            query.addCriteria(Criteria.where("phoneNum").is(param.getPhoneNum()));
        }
        if (StringUtils.isNotEmpty(param.getUserName())) {
            query.addCriteria(Criteria.where("userName").regex(param.getUserName(), "i")); // 使用正则表达式进行模糊匹配，"i"表示不区分大小写
        }
        if (StringUtils.isNotEmpty(param.getCallMonth())) {
            query.addCriteria(Criteria.where("callMonth").is(param.getCallMonth()));
        }
        log.info("mongo 查询参数：{}", JSONObject.toJSONString(query));
        // 分页
        long total = mongoTemplate.count(query, FddAuthMonthCost.class);
        log.info("mongo 总条数：{}", total);
        Pageable pageable = PageRequest.of(param.getPageNum() -1, param.getPageSize(), Sort.by(Sort.Direction.fromString("DESC"), "callMonth"));
        query.with(pageable);
        List<FddAuthMonthCost> fddAuthMonthCosts = mongoTemplate.find(query, FddAuthMonthCost.class);
        List<FddAuthMonthCostVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(fddAuthMonthCosts)){
            for (FddAuthMonthCost cost : fddAuthMonthCosts) {
                FddAuthMonthCostVo vo = new FddAuthMonthCostVo();
                BeanUtils.copyProperties(cost,vo);
                list.add(vo);
            }
        }
        ApiPageResult<FddAuthMonthCostVo> resultApiPageResult = ApiPageResult.<FddAuthMonthCostVo>builder()
                .currentPage(param.getPageNum())
                .pageSize(param.getPageSize())
                .totalPage((int)Math.ceil((double) total / param.getPageSize()))
                .total((int)total)
                .build();
        resultApiPageResult.setList(list);
        return resultApiPageResult;
    }

    @Override
    public void fddAuthMonthExport(FddCostDetailReq param) {
        ExportRecord dto = buildExportRecord(Constant.EXPORT_TYPE.FDD_AUTH_MONTH, "法大大实名认证月汇总");
        //组装查询条件
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getPhoneNum())) {
            query.addCriteria(Criteria.where("phoneNum").is(param.getPhoneNum()));
        }
        if (StringUtils.isNotEmpty(param.getUserName())) {
            query.addCriteria(Criteria.where("userName").regex(param.getUserName(), "i")); // 使用正则表达式进行模糊匹配，"i"表示不区分大小写
        }
        if (StringUtils.isNotEmpty(param.getCallMonth())) {
            query.addCriteria(Criteria.where("callMonth").is(param.getCallMonth()));
        }
        query.with(Sort.by(Sort.Direction.fromString("DESC"), "callMonth"));
        //执行导出
        mongoStreamExport(dto, query, FddAuthMonthCost.class, FddAuthMonthExportVo.class, fdd-> BeanUtil.toBean(fdd, FddAuthMonthExportVo.class));
    }

    @Override
    public ApiPageResult<FddContractDayCostVo> contractPage(FddCostDetailReq param) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getCallDay())) {
            query.addCriteria(Criteria.where("callDay").is(param.getCallDay()));
        }
        log.info("moggo count param:{}", JSONObject.toJSONString(param));
        long total = mongoTemplate.count(query, FddContractDayCost.class);
        log.info("moggo count total:{}", total);
        // 分页
        Pageable pageable = PageRequest.of(param.getPageNum() -1, param.getPageSize(), Sort.by(Sort.Direction.fromString("DESC"), "callDay"));
        query.with(pageable);
        List<FddContractDayCost> fddContractDayCosts = mongoTemplate.find(query, FddContractDayCost.class);
        log.info("moggo count list:{}", JSONObject.toJSONString(fddContractDayCosts));
        List<FddContractDayCostVo> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(fddContractDayCosts)){
            for (FddContractDayCost cost : fddContractDayCosts) {
                FddContractDayCostVo vo = new FddContractDayCostVo();
                BeanUtils.copyProperties(cost,vo);
                list.add(vo);
            }
        }
        ApiPageResult<FddContractDayCostVo> resultApiPageResult = ApiPageResult.<FddContractDayCostVo>builder()
                .currentPage(param.getPageNum())
                .pageSize(param.getPageSize())
                .totalPage((int)Math.ceil((double) total / param.getPageSize()))
                .total((int)total)
                .build();
        resultApiPageResult.setList(list);
        return resultApiPageResult;
    }

    @Override
    public void fddContarctMonthExport(FddCostDetailReq param) {
        ExportRecord dto = buildExportRecord(Constant.EXPORT_TYPE.FDD_CONTARCT_MONTH, "法大大合同签署月汇总");
        //构建查询参数
        Query query = new Query();
        if (StringUtils.isNotEmpty(param.getCallDay())) {
            query.addCriteria(Criteria.where("callDay").is(param.getCallDay()));
        }
        query.with(Sort.by(Sort.Direction.fromString("DESC"), "callDay"));
        //执行导出
        mongoStreamExport(dto, query, FddContractDayCost.class, FddContractDayExportVo.class, fdd-> BeanUtil.toBean(fdd, FddContractDayExportVo.class));
    }

    /**
     * 构建导出记录
     * @param exportType
     * @param fileName
     * @return
     */
    private ExportRecord buildExportRecord(String exportType, String fileName) {
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        return new ExportRecord().setExportType(exportType)
                .setFileName(sessionInfo.getCompanyName()+ StringPool.DASH+fileName)
                .setCreater(sessionInfo.getId());
    }

    /**
     * mongo流式导出改造
     * @param dto
     * @param query
     * @param sourceClass
     * @param targetClas
     * @param func
     * @param <T>
     * @param <R>
     */
    private <T,R> void mongoStreamExport(ExportRecord dto, Query query, Class<T> sourceClass, Class<R> targetClas, Function<T, R> func) {
        //单次拉取10000条
        query.cursorBatchSize(10000);
        CloseableIterator<T> cursor = mongoTemplate.stream(query, sourceClass);
        //执行导出操作
        try {
            reportTemplate.exportBigExcel(targetClas, dto, page -> {
                //该方法是异步执行，异常需要再次捕获
                try {
                    List<R> fdds = new ArrayList<>();
                    while(cursor.hasNext()){
                        T fdd = cursor.next();
                        R vo =func.apply(fdd);
                        //添加到集合中，单页上限500
                        fdds.add(vo);
                        if(fdds.size() >= 500){
                            break;
                        }
                    }
                    //数据拉取完成，关闭游标
                    if (fdds.isEmpty()){
                        cursor.close();
                        log.info("fdd mongodb流式导出数据完成，关闭游标");
                    }
                    return fdds;
                } catch (Exception e) {
                    log.error("fdd mongodb流式导出数据异常并关闭游标:",e);
                    cursor.close();
                    throw new BusinessException(e.getMessage());
                }
            });
        } catch (Exception e) {
            cursor.close();
            log.error("执行fdd财务对账导出异常并关闭游标",e);
        }
    }
}
