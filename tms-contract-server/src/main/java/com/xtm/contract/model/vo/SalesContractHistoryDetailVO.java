package com.xtm.contract.model.vo;

import lombok.Data;

/**
 * 历史记录详情
 */
@Data
public class SalesContractHistoryDetailVO {

    private String beforeContent;//变更前的历史内容;
    private String afterContent;//变更后的内容;
    private String contractCode;//变更后的内容;

}