package com.xtm.contract.model.vo.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 法大大合同签署月汇总Vo
 * @author lya
 */
@Data
public class FddContractDayExportVo {
    
    @Excel(name = "对接公司")
    private String companyName;

    @Excel(name = "项目")
    private String project;

    @Excel(name = "日期")
    private String callDay;

    @Excel(name = "使用量")
    private Integer callTimes;

    @Excel(name = "单价")
    private String unitPrice;

    @Excel(name = "扣费")
    private String cost;
}


