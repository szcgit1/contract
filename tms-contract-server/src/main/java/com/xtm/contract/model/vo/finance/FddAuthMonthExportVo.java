package com.xtm.contract.model.vo.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 法大大实名认证月汇总Vo
 * @author lya
 */
@Data
public class FddAuthMonthExportVo {
    
    @Excel(name = "对接公司")
    private String companyName;

    @Excel(name = "项目")
    private String project;

    @Excel(name = "姓名")
    private String userName;

    @Excel(name = "手机号")
    private String phoneNum;

    @Excel(name = "认证月份")
    private String callMonth;

    @Excel(name = "认证次数")
    private Integer callTimes;

    @Excel(name = "单价")
    private String unitPrice;

    @Excel(name = "费用")
    private String cost;
}


