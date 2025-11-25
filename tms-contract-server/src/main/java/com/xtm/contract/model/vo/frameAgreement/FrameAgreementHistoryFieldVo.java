package com.xtm.contract.model.vo.frameAgreement;

import com.xtm.contract.model.domain.FrameAgreement;
import lombok.Data;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-09-18  20:12
 *@title: FrameAgreementHistoryFieldVo
 */
@Data
public class FrameAgreementHistoryFieldVo extends FrameAgreement {

    /**
     * 停用标记 0: 未停用 1: 已停用
     */
    private Boolean disabled;

    /*********************************** 关联销售合同 ******************************************/

    /**
     * 关联合同编码
     */
    private String reContractCode;

    /*********************************** nc子表 ******************************************/

    List<FrameAgreementSubHistoryVo> subList ;

    /**
     * 关联来源 0: NC推送销售合同自动匹配框架合同 1: 页面手动操作 框架合同关联销售合同
     */
    private Integer reSource;
}
