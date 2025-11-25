package com.xtm.contract.model.vo.finance;

import lombok.Data;

import java.util.Date;

/**
 * 法大大认证月费用统计
 */
@Data
public class FddAuthMonthCostVo {
    private String id;
    //对接公司
    private String companyName;
    //项目
    private String project;
    //用户名称
    private String userName;
    private String userId;
    //手机号
    private String phoneNum;
    //调用月份
    private String callMonth;
    //调用次数
    private Integer callTimes;
    //费用
    private String cost;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    private String unitPrice;


}