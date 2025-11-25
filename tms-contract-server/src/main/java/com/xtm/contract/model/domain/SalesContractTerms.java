package com.xtm.contract.model.domain;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xtm.v1.common.model.TransitionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 物流合同条款表
 * </p>
 *
 * @author 孙志超
 * @since 2025-09-10
 */
@Data
@TableName(value ="sales_contract_terms")
@EqualsAndHashCode(callSuper = false)
public class SalesContractTerms extends TransitionEntity<SalesContractTerms> {

    private static final long serialVersionUID = 1L;

    /**
     * 物流合同id
     */
    private Long salesContractId;

    /**
     * 条款编号
     */
    private String termCode;

    /**
     * 条款名称
     */
    private String termName;

    /**
     * 条款内容
     */
    private String termContent;

    /**
     * 数据版本
     */
    private Integer version;

    /**
     * 删除标记 0: 未删除 1: 已删除
     */
    private Boolean deleted;

    /**
     * 停用标记 0: 未停用 1: 已停用
     */
    private Boolean disabled;

    /**
     * 创建人标识
     */
    private String createId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人标识
     */
    private String modifyId;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


}
