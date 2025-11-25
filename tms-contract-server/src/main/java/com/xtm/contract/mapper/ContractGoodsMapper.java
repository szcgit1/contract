package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.ContractGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/26 17:10
 * @desc 合同货物
 */
@Mapper
public interface ContractGoodsMapper extends BaseMapper<ContractGoods> {

    /**
     * 查询迁移的合同附件
     *
     * @param contractId
     */
    List<ContractGoods> getMigrateContractGoods(@Param("contractId") String contractId, @Param("suffix") String suffix);
}
