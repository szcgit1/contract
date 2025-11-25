package com.xtm.contract.model.param;

import lombok.Data;

@Data
public class NcUpdateStateParam {

    /**
     * nc主键id
     */
    private String salesContractId;

    /**
     * 状态 0：启用 1禁用
     */
    private Integer contractStatus;
}
