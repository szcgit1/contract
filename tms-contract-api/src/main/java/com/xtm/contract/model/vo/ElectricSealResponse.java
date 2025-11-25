package com.xtm.contract.model.vo;

import lombok.Data;

/***
 * Description
 * Date 2025/8/1 9:11
 * Version 1.0
 * @author zhangshichuang
 */
@Data
public class ElectricSealResponse {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 签章开关标识（e签宝：0  法大大：1）
     */
    private Integer signSwitchTag;
}
