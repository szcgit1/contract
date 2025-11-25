package com.xtm.contract.model.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 法大大消费详情
 */
@Data
@Document(collection = "xn_fdd_cost_detail")
public class FddCostDetail {
    @Id
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
    @Indexed
    private String phoneNum;
    //运单号
    private String contractCode;
    //调用时间
    private Long callTime;
    @Indexed
    private String callMonth;
    @Indexed
    private String callDay;
    //状态，false：不通过，true：通过
    private boolean success;
    //费用
    private String cost;
    //创建时间
    private Long createTime;
    //类型，auth、contract
    private String type;
    //业务标志
    private String busId;

}


