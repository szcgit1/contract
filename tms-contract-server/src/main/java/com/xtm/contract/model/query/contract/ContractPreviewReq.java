package com.xtm.contract.model.query.contract;

import lombok.Data;

@Data
public class ContractPreviewReq {
    /**
     * 合同id（1 2） 业务申报id(3 4)
     */
    private String contractId;
    /**
     * 类型  1 预览电子签章 2预览框架合同 3 预览 无船/无车tob 委托方合同 4预览无船to托运方合同 5甘肃无车tob委托方合同 6甘肃无车to托运方合同
     */
    private Integer type;
}
