package com.xtm.contract.model.param;

import lombok.Data;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-01  21:03
 *@Description:
 *@title: ContractParam
 */
@Data
public class UpdateContractDataParam {

    /**
     * 修改值对象
     */
    ContractParam contractSet;

    /**
     * 过滤值对象
     */
    ContractParam contractFilter;

}
