package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.param.SalesContractListParam;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementRelateSaleContractVo;
import com.xtm.contract.model.vo.SalesContractListVO;
import com.xtm.contract.model.vo.SalesContractVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  19:57
 *@Description: 物流合同Mapper
 */
@Mapper
public interface SalesContractMapper extends BaseMapper<SalesContract> {

    /**
     * 根据框架合同id查询协议合同的关联物流合同
     * @param agreementIds
     * @return
     */
    List<SalesContractVo> queryByAgreementIds(@Param("agreementIds") List<Long> agreementIds);

    /**
     * 根据框架合同id，查询关联物流合同
     * @param agreementIds
     * @return
     */
    List<FrameAgreementRelateSaleContractVo> queryRelateContractByAgreementId(@Param("agreementIds") List<Long> agreementIds);

    /**
     * 根据物流合同查询启用且未绑定物流合同协议的物流合同接口
     * @param contractCode
     * @return
     */
    List<SalesContractVo> getEnableUnRelateContractByCode(@Param("contractCode") String contractCode);
    /**
     * 销售合同列表查询
     */
    List<SalesContractListVO> querySalesContractPageList(@Param("param") SalesContractListParam param);

    /**
     * @param shippingCompanyId nc发运公司主键
     */
    List<SalesContract> getSalesContractNoBusiSourceByShippingCompany(@Param("shippingCompanyId") String shippingCompanyId);
}
