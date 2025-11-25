package com.xtm.contract.model.vo.frameAgreement;

import lombok.Data;

/**
 * 历史记录详情
 */
@Data
public class FrameAgreementHistoryDetailVo {

    private String beforeContent;//变更前的历史内容;
    private String afterContent;//变更后的内容;
    private String code;//变更后的内容;

}