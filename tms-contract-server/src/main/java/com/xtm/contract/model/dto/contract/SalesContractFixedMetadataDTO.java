package com.xtm.contract.model.dto.contract;

import com.xtm.contract.model.bo.FixedMetaData;
import lombok.Data;

/***
 *@Author: 孙志超
 *@CreateTime: 2025-10-10  20:30
 */
@Data
public class SalesContractFixedMetadataDTO extends FixedMetaData<SalesContractFixedMetadataDTO> {

    /**
     * 合同协议名称
     */
    private String contractName;

    /**
     * 合同协议编号
     */
    private String contractCode;

    /**
     * 修改前合同状态 停用标记 0: 未停用 1: 已停用
     */
    private Boolean beforeDisabled;
    /**
     * 修改后合同状态 停用标记 0: 未停用 1: 已停用
     */
    private Boolean afterDisabled;


}
