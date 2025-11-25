package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EAccountInfoRes {
    /**个人账号id*/
    private String accountId;
    /**机构账号Id*/
    private String orgId;
    private String name;
    private String idType;
    private String idNumber;
    private String mobile;
    private String email;
    private String thirdPartyUserId;
    /**
     * 实名认证状态
     * true-已实名
     * false-未实名
     */
    private String status;
}
