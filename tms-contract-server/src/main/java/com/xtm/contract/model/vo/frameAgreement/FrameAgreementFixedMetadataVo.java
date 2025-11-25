package com.xtm.contract.model.vo.frameAgreement;

import com.xtm.contract.model.bo.FixedMetaData;
import lombok.Data;

/***
 *@Author: 王磊
 *@CreateTime: 2025-09-18  20:20
 *@title: FrameAgreementFixedMetadataVo
 */
@Data
public class FrameAgreementFixedMetadataVo extends FixedMetaData<FrameAgreementFixedMetadataVo> {

    /**
     * 合同协议名称
     */
    private String name;

    /**
     * 合同协议编号
     */
    private String code;

    /**
     * 修改前合同状态 停用标记 0: 未停用 1: 已停用
     */
    private Boolean beforeDisabled;
    /**
     * 修改后合同状态 停用标记 0: 未停用 1: 已停用
     */
    private Boolean afterDisabled;

    /**
     * 关联销售合同来源 0: NC推送销售合同自动匹配框架合同 1: 页面手动操作 框架合同关联销售合同
     */
    private Integer reSource;

}
