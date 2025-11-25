package com.xtm.contract.service.impl;
import com.xtm.common.service.impl.SuperServiceImpl;
import com.xtm.contract.mapper.SalesContractGoodsMapper;
import com.xtm.contract.model.domain.SalesContractGoods;
import com.xtm.contract.model.dto.GoodsQuantityDTO;
import com.xtm.contract.service.SalesContractGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 物流合同货物表 服务实现类
 * </p>
 *
 * @author 孙志超
 * @since 2025-09-10
 */
@Service
public class SalesContractGoodsServiceImpl extends SuperServiceImpl<SalesContractGoodsMapper, SalesContractGoods> implements SalesContractGoodsService {

    @Resource
    private SalesContractGoodsMapper salesContractGoodsMapper;

    /**
     * 根据物流合同id查询货物数量,按照货物id分组求和
     */
    @Override
    public List<GoodsQuantityDTO> sumGoodsQuantityByContractId(List<Long> contractIds) {
        return salesContractGoodsMapper.sumGoodsQuantityByContractId(contractIds);
    }
}
