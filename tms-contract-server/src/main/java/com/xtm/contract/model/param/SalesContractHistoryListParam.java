package com.xtm.contract.model.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 销售合同协议历史记录入参
 */
@Data
public class SalesContractHistoryListParam implements Serializable {

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
     * 合同名称编号
     */
    private String contractNameCode;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 合同状态 停用标记 0: 未停用 1: 已停用
     */
    private Boolean disabled;

}