package com.xtm.contract.service;

import com.xtm.common.service.SuperService;
import com.xtm.contract.model.domain.SalesContractGoods;
import com.xtm.contract.model.dto.GoodsQuantityDTO;

import java.util.List;

/**
 * <p>
 * 物流合同货物表 服务类
 * </p>
 *
 * @author 孙志超
 * @since 2025-09-10
 */
public interface SalesContractGoodsService extends SuperService<SalesContractGoods> {


    /**
     * 根据物流合同id查询货物数量,按照货物id分组求和
     */
    List<GoodsQuantityDTO> sumGoodsQuantityByContractId(List<Long> contractIds);
}
