package com.xtm.contract.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xtm.contract.mapper.ContractMigrateMapper;
import com.xtm.contract.model.domain.ContractMigrate;
import com.xtm.contract.service.ContractMigrateService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 合同(已迁移表) 服务实现类
 * </p>
 *
 * @author khj
 * @since 2024-04-15
 */
@Service
public class ContractMigrateServiceImpl extends ServiceImpl<ContractMigrateMapper, ContractMigrate> implements ContractMigrateService {

}
