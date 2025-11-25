package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EAuthenticationUrlRes {
    /**认证流程Id*/
    private String flowId;
    /**个人实名认证短链接，有效期30天*/
    private String shortLink;
    /**个人实名认证长链接，链接永久有效*/
    private String url;
}
