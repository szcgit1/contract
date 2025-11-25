package com.xtm.contract.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementFixedMetadataVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 *@Author: 王磊
 *@CreateTime: 2025-09-18  21:13
 *@title: FrameAgreementHistoryDto
 */
@Data
public class FrameAgreementHistoryDto extends FrameAgreementFixedMetadataVo implements Serializable {

    private String id;//主键编号;
    private String business_type;//业务类型：如调度配载(dispatch),后面会对应一个枚举;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date operation_time;

    /**
     * 操作人姓名
     */
    private String operator;

    /**
     * 业务变更信息
     */
    private ChangesDto changes;

    /**
     * 操作类型(创建/更新/作废)
     */
    private String operation_type;
}