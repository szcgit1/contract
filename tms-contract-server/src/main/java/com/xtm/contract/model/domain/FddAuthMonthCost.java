package com.xtm.contract.model.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 法大大认证月费用统计
 */
@Data
@Document(collection = "xn_fdd_auth_month_cost")
public class FddAuthMonthCost {
    @Id
    private String id;
    //对接公司
    private String companyName;
    //项目
    private String project;
    //用户名称
    private String userName;
    @Indexed
    private String userId;
    //手机号
    private String phoneNum;
    //调用月份
    @Indexed
    private String callMonth;
    //调用次数
    private Integer callTimes;
    //费用
    private String cost;
    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime;
    private String unitPrice;


}