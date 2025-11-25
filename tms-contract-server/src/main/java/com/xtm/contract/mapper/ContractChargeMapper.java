package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xtm.contract.model.domain.ContractCharge;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/26 17:10
 * @desc 合同费用
 */
@Mapper
public interface ContractChargeMapper extends BaseMapper<ContractCharge> {

}
