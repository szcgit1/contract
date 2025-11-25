package com.xtm.contract.model.param.frameAgreement;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 框架合同协议表分页入参
 */
@Data
public class FrameAgreementParam implements Serializable {

    private static final long serialVersionUID = 2710789183615131439L;

    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private Integer pageNum;

    /**
     * 每页数据量
     */
    @NotNull(message = "每页数据量不能为空")
    private Integer pageSize;

    /**
     * 合同协议编码
     */
    private String code;

    /**
     * 发运组织统一社会信用代码
     */
    private String shippingUscc;

    /**
     * 客户统一社会信用代码
     */
    private String customerUscc;

    /**
     * 销售组织统一社会信用代码
     */
    private String saleOrgUscc;

    /**
     * 产品线名称
     */
    private String productLineName;

    /**
     * 停用标记 0: 未停用 1: 已停用
     */
    private Integer disabled;

    /**
     * 虚拟年度协议标识 0：否 1：是
     */
    private Integer virtualTag;

    /**
     * 业务来源 0:丰南 1:中铁 2:中重 99:本平台
     */
    private List<Integer> busiSource;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
