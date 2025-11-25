package com.xtm.contract.model.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 法大大合同签署月费用统计
 */

@Data
@Document(collection = "xn_fdd_contract_day_cost")
public class FddContractDayCost {
    @Id
    private String id;
    //对接公司
    private String companyName;
    //项目
    private String project;
    //调用日期
    @Indexed
    private String callDay;
    //使用量
    private Integer callTimes;
    //单价
    private String unitPrice;
    //费用
    private String cost;

    //创建时间
    private Long createTime;
    //更新时间
    private Long updateTime;

}