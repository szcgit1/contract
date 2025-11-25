package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description:    企业组织基本信息对象
* @Author:         mxr
* @CreateDate:     2021-03-05 15:05
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EOrganInfoRes {
    /**组织用户id*/
    private String accountId;
    /**组织名称*/
    private String name;
    /**组织证件号*/
    private String certNo;
    /**组织证件类型*/
    private String certType;
    /**法定代表人姓名*/
    private String legalRepName;
    /**法定代表人国籍/地区*/
    private String legalRepNationality;
    /**法定代表人证件号*/
    private String legalRepCertNo;
    /**法定代表人证件类型*/
    private String legalRepCertType;
}
