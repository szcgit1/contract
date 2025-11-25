package com.xtm.contract.model.vo.frameAgreement;

import lombok.Data;

/**
 * 历史记录-响应内容
 */
@Data
public class FrameAgreementHistoryListVo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 合同协议名称
     */
    private String name;

    /**
     * 合同协议编号
     */
    private String code;

    /**
     * 合同状态 停用标记 0: 未停用 1: 已停用
     */
    private String disabled;

    /**
     * 操作人姓名
     */
    private String operateUser;

    /**
     * 修改操作时间
     */
    private String operateTime;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 调整内容
     */
    private String adjustContent;

}