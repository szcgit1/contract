package com.xtm.contract.model.vo.frameAgreement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xtm.contract.model.vo.FileInfo;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:28
 *@Description: 框架合同协议表Vo
 */
@Data
public class FrameAgreementDetailVo {

    /**
     * 合同协议编号
     */
    private String code;

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
     * 销售组织
     */
    private String saleOrg;

    /**
     * 销售组织统一社会信用代码
     */
    private String saleOrgUscc;

    /**
     * 年份
     */
    private String year;

    /**
     * 总协议量
     */
    private BigDecimal totalVolume;

    /**
     * 产品线编号id
     */
    private Long productLineId;

    /**
     * 产品线编号
     */
    private String productLineCode;

    /**
     * 产品线名称
     */
    private String productLineName;

    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 失效日期
     */
    private LocalDateTime expiryDate;

    /**
     * 虚拟年度协议标识 0：否 1：是
     */
    private Integer virtualTag;

    /**
     * 虚拟协议绑定的合同协议id  合同协议 1:N 虚拟协议
     */
    private Long parentId;

    /**
     * 协议号
     */
    private String num;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合同状态 0：启用 1：禁用
     */
    private Integer disabled;

    /**
     * 业务来源 0:丰南 1:中铁 2:中重 99:本平台
     */
    private Integer busiSource;

    /**
     * 系统来源 0:tms 1:nc 2:新网
     */
    private Integer systemSource;

    /**
     * nc主表主键
     */
    private String mainId;

    /**
     * 删除标记 0: 未删除 1: 已删除
     */
    private Integer deleted;

    /**
     * 关联虚拟年度协议
     */
    private String reVirtualCode;

    /**
     * 虚拟年度协议关联框架合同名称  虚拟标识 + 关联虚拟年度协议
     */
    private String reVirtualName;

    /**
     * 合同协议id关联的销售合同
     */
    private List<FrameAgreementRelateSaleContractVo> relateContractVoList;

    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date billDate;
}
