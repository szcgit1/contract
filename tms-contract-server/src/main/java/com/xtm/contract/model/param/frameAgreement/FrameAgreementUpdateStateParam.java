package com.xtm.contract.model.param.frameAgreement;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 框架合同协议修改合同状态入参
 */
@Data
public class FrameAgreementUpdateStateParam implements Serializable {

    /**
     * 合同状态 0：启用 1：禁用
     */
    @NotNull(message = "合同状态不能为空")
    private Integer disabled;

    /**
     * 合同协议状态集合
     */
    private List<String> agreementIds;

}
