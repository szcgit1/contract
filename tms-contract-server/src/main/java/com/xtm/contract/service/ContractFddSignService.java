package com.xtm.contract.service;

import com.xtm.thirdparty.auth.model.param.FddUserBO;

import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-01  14:37
 *@Description: 法大大合同服务层接口
 *@title: ContractFddSignService
 */
public interface ContractFddSignService {


    List<String> getOpenIds(String operatorId, Integer accountType);

    /**
     * 查询openId和用户信息(个人用户情况)
     * @param operatorId
     * @param accountType
     * @param fddUserBO
     */
    void queryOpenIdsAndUserInfo(String operatorId, Integer accountType, FddUserBO fddUserBO);
}
