package com.xtm.contract.model.param;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 保存NC框架合同协议入参
 */
@Data
public class NcFrameAgreementSaveParam implements Serializable {


    private static final long serialVersionUID = 2710789183615131439L;

    /**
     * 接口来源  0：创建 1：修改
     */
    private Integer interSource;

    /**
     * 合同协议编号
     */
    private String code;

    /**
     * 单据日期
     */
    private LocalDateTime createTime;

    /**
     * 发运组织
     */
    private String shipping;

    /**
     * 发运组织统一社会信用代码
     */
    private String shippingUscc;

    /**
     * 发运组织统一主键
     */
    private String shippingMain;

    /**
     * 客户
     */
    private String customer;

    /**
     * 客户统一社会信用代码
     */
    private String customerUscc;

    /**
     * 产品线编号
     */
    private String productLineCode;

    /**
     * 年份
     */
    private String year;

    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    private LocalDateTime expiryDate;

    /**
     * 总协议量
     */
    private BigDecimal totalVolume;

    /**
     * 审批状态 0：启用 1：禁用
     */
    private Integer disabled;

    /**
     * 虚拟年度协议标识 0：否 1：是
     */
    private Integer virtualTag;

    /**
     * 销售组织
     */
    private String saleOrg;

    /**
     * 销售组织统一社会信用代码
     */
    private String saleOrgUscc;

    /**
     * 备注
     */
    private String remark;

    /**
     * 协议号
     */
    private String num;

    /**
     * nc主表主键
     */
    private String mainId;

    /**
     * 系统来源 0:tms 1:nc 2:新网
     */
    private Integer systemSource;

    /**
     * 框架合同协议子表
     */
    private List<NcFrameAgreementSubParam> agreementSubList;

}
