package com.xtm.contract.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.SalesContractGoods;
import com.xtm.contract.model.dto.GoodsQuantityDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物流合同货物表 Mapper 接口
 * </p>
 *
 * @author 孙志超
 * @since 2025-09-10
 */
@Mapper
public interface SalesContractGoodsMapper extends BaseMapper<SalesContractGoods> {

    /**
     * 根据物流合同id查询货物数量,按照货物id分组求和
     */
    List<GoodsQuantityDTO> sumGoodsQuantityByContractId(@Param("contractIds") List<Long> contractIds);
}
