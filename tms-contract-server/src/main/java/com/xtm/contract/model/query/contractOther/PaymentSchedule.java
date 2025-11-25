package com.xtm.contract.model.query.contractOther;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PaymentSchedule extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty("付款方式:现付1021000,回单付1021020,月结1021030 1021040 到付")
    private Integer paymentMode;

    private BigDecimal price;

    private String documentId;

    private Integer documentType;

    private Integer ver;

    private String creater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String modifier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    private Integer voucherType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settleDate;

    private String busiAuditDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime busiAuditTime;

    private Integer busiAuditStatus;

    private String busiAuditUserId;

    private String carryBusiAuditDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime carryBusiAuditTime;

    private Integer carryBusiAuditStatus;

    private String carryBusiAuditUserId;

    private String busiAuditCompanyId;

    private String carryBusiAuditCompanyId;

    /**
     * 是否自动调整结算价
     */
    private Integer isAutoAdjust;

    /**
     * 付款比例(%)
     */
    private BigDecimal priceRatio;


}
