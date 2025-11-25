package com.xtm.contract.model.vo.finance;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 法大大消费详情导出Vo
 * @author lya
 */
@Data
public class FddCostDetailExportVo {
    
    @Excel(name = "对接公司")
    private String companyName;

    @Excel(name = "项目")
    private String project;

    @Excel(name = "姓名")
    private String userName;

    @Excel(name = "手机号")
    private String phoneNum;

    @Excel(name = "运单号")
    private String contractCode;

    @Excel(name = "使用时间")
    private Date callTime;

    @Excel(name = "返回状态")
    private String success;

    @Excel(name = "费用")
    private String cost;
}


