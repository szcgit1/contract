package com.xtm.contract.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.ContractAttach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/3/25 17:23
 * @version: 1.0
 */
@Mapper
public interface ContractAttachMapper extends BaseMapper<ContractAttach> {
    /**
     * 修改合同附件状态
     * @param contractId
     */
    void updateContractAttachStatus(@Param("contractId")String contractId);

    /**
     * 查询迁移的合同附件
     *
     * @param contractId
     */
    List<ContractAttach> getMigrateContractAttach(@Param("contractId") String contractId, @Param("suffix") String suffix);
}
