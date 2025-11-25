package com.xtm.contract.service;

import com.xtm.common.model.Result;
import com.xtm.common.service.SuperService;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.param.SalesContractListParam;
import com.xtm.contract.model.param.NcSalesContractAddOrUpdateParam;
import com.xtm.contract.model.param.NcUpdateStateParam;
import com.xtm.contract.model.vo.SalesContractGoodsVO;
import com.xtm.contract.model.param.UpdateAccumulateOrdersMainQuantityParam;
import com.xtm.contract.model.vo.SalesContractDetailVO;
import com.xtm.contract.model.vo.SalesContractVo;
import com.xtm.contract.model.vo.SalesContractListVO;

import java.util.List;

/**
 * 物流合同表服务接口
 */
public interface SalesContractService extends SuperService<SalesContract> {

    /**
     * 根据物流合同查询启用且未绑定物流合同协议的物流合同接口
     * @param contractCode 合同的编号
     */
    List<SalesContractVo> getEnableUnRelateContractByCode(String contractCode);

    /**
     * NC更新累计订单主数量接口
     */
    Result<String> updateAccumulateOrdersMainQuantity(UpdateAccumulateOrdersMainQuantityParam param);


    /**
     * nc新增销售合同
     */
    Result<String> ncCreate(NcSalesContractAddOrUpdateParam param);


    /**
     * nc修改销售合同
     */
    Result<String> ncUpdate(NcSalesContractAddOrUpdateParam param);


    /**
     * 查看销售合同详情
     */
    SalesContractDetailVO getDetail(String id);
    /**
     * 销售合同列表查询
     */
    ApiPageResult<SalesContractListVO> querySalesContractPageList(SalesContractListParam param);

    /**
     * nc禁用/启用
     */
    Result<String> ncUpdateState(NcUpdateStateParam param);

    /**
     * 根据合同id查询货物信息
     */
    List<SalesContractGoodsVO> getSalesContractGoods(String contractId);

    /**
     * 查询最新版本物流合同基本信息
     */
    SalesContractDetailVO getContractInfo(String contractCode);

    /**
     * 判断销售合同是否存在（供前端日志详情展示【重新创建】按钮使用）
     * @param salesContractId nc销售合同ID
     * @return Boolean true:表示已存在 false:无单据
     */
    Boolean querySalesContractBySalesContractIdAndVersion(String salesContractId,Integer version);

    /**
     * 根据nc销售合同id获取销售合同信息
     * @param salesContractId nc销售合同ID
     */
    SalesContract getSalesContractBySalesContractId(String salesContractId);
}