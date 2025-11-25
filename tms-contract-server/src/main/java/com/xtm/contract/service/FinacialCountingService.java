package com.xtm.contract.service;


import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.query.finania.FddCostDetailReq;
import com.xtm.contract.model.vo.finance.FddAuthMonthCostVo;
import com.xtm.contract.model.vo.finance.FddContractDayCostVo;
import com.xtm.contract.model.vo.finance.FddCostDetailVo;


public interface FinacialCountingService{
     ApiPageResult<FddCostDetailVo> page(FddCostDetailReq fddCostDetailRreq);

     /**
      * 法大大详情导出
      * @param fddCostDetailRreq
      * @return
      */
     void fddDetailExport(FddCostDetailReq fddCostDetailRreq);

     ApiPageResult<FddAuthMonthCostVo> authPage(FddCostDetailReq fddCostDetailRreq);

     /**
      * 法大大实名认证月汇总导出
      * @param fddCostDetailRreq
      * @return
      */
     void fddAuthMonthExport(FddCostDetailReq fddCostDetailRreq);

     ApiPageResult<FddContractDayCostVo> contractPage(FddCostDetailReq fddCostDetailRreq);

     /**
      * 法大大合同签署月汇总导出
      * @param fddCostDetailRreq
      * @return
      */
     void fddContarctMonthExport(FddCostDetailReq fddCostDetailRreq);
}
