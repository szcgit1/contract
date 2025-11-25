package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.ContractMigrate;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同(已迁移表) Mapper 接口
 * </p>
 *
 * @author khj
 * @since 2024-04-15
 */
@Mapper
public interface ContractMigrateMapper extends BaseMapper<ContractMigrate> {

}
