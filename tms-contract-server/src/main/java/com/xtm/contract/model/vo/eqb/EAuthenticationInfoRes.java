package com.xtm.contract.model.vo.eqb;
/**
* @Description:    认证信息
* @Author:         mxr
* @CreateDate:     2021-03-05 15:02
*/
public class EAuthenticationInfoRes {
    /**认证流程ID*/
    private String flowId;
    /**
     * 认证流程状态,
     * INIT已发起，该状态下e签宝不会计费；
     * ING进行中
     * SUCCESS已成功
     * FAIL已失败
     */
    private String status;
    /**认证主体类型*/
    private String objectType;
    /**流程创建时间*/
    private Integer startTime;
    /**流程最后更新时间*/
    private Integer endTime;
    /**认证失败时的原因*/
    private String failReason;
    /**实名通过时使用的认证类型*/
    private String authType;
    /**企业组织基本信息对象*/
    private EOrganInfoRes organInfo;
    /**个人基本信息对象*/
    private EIndivInfoRes indivInfo;
}
