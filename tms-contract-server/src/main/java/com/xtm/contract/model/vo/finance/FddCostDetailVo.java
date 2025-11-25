package com.xtm.contract.model.vo.finance;

import lombok.Data;

/**
 * 法大大消费详情
 */
@Data

public class FddCostDetailVo {

    private String id;
    //对接公司
    private String companyName;
    //项目
    private String project;
    //用户名称
    private String userId;
    //用户名称
    private String userName;
    //手机号
    private String phoneNum;
    //运单号
    private String contractCode;
    //调用时间
    private String callTime;
    private String callMonth;
    private String callDay;
    //状态，false：不通过，true：通过
    private String success;
    //费用
    private String cost;
    //创建时间
    private String createTime;
    //类型，auth、contract
    private String type;
    //业务标志
    private String busId;

}


