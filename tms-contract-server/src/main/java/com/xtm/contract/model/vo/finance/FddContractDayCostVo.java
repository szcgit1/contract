package com.xtm.contract.model.vo.finance;


import lombok.Data;

import java.util.Date;

/**
 * 法大大合同签署月费用统计
 */

@Data
public class FddContractDayCostVo {
    private String id;
    //对接公司
    private String companyName;
    //项目
    private String project;
    //调用日期
    private String callDay;
    //使用量
    private Integer callTimes;
    //单价
    private String unitPrice;
    //费用
    private String cost;

    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}