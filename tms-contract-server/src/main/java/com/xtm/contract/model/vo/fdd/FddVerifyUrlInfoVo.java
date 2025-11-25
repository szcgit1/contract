package com.xtm.contract.model.vo.fdd;

import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.vo.fdd.FddVerifyUrlInfoVo
 * @author: wwh
 * @create: 2025-04-09 11:17
 * @description: 法大大认证返回信息
 **/
@Data
public class FddVerifyUrlInfoVo implements Serializable {
    private static final long serialVersionUID = -3310238334351904679L;
    /**
     * 认证url
     */
    private String verifyUrl;
    /**
     * 认证状态：1- 已认证，0-未认证
     */
    private String verifyStatus;
}
