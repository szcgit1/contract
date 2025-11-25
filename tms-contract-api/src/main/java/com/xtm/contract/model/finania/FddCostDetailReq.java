package com.xtm.contract.model.finania;


import com.xtm.contract.model.common.Page;
import lombok.Data;

/**
 * 法大大消费详情
 */
@Data
public class FddCostDetailReq extends Page {

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
    //调用时间
    private String callTime;

    private String callMonth;

    private String callDay;
    private String startTime;
    private String endTime;
}


