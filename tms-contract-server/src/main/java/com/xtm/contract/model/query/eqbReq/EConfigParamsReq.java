package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

import java.util.List;

/**
* @Description:    认证配置信息
* @Author:         mxr
* @CreateDate:     2021-03-05 11:36
*/
@Data
public class EConfigParamsReq {
    /**指定个人认证页面上不可修改的基本信息*/
    private List<String> indivUneditableInfo;
}
