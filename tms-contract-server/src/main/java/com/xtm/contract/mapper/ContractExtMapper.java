package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.ContractExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractExtMapper extends BaseMapper<ContractExt> {
    /**
     * 查询迁移的合同扩展表
     *
     * @param contractId
     */
    List<ContractExt> getMigrateContractExt(@Param("contractId") String contractId, @Param("suffix") String suffix);
}
