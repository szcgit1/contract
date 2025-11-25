package com.xtm.contract.model.query.contractOther;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class TransportChargeItem extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private BigDecimal amount;

    private String description;

    private Integer methodType;

    /**
     * 起步算法描述(json格式，起步价，起步值，超过起步值单价)
     */
    private String startingPriceMethod;

    private String transportChargeId;

    private String accountSubjectId;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    private BigDecimal subtotalPrice;

    private String creater;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String modifier;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

    private Integer ver;


}
