package com.xtm.contract.service;

import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractDto;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractModifyStatusDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractGoodsQuantityDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractModifyStatusDto;

/**
 * 三方合同策略
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-08-29 21:04
 */
public interface ThirdPartyContractStrategy {
    
    /**
     * 策略模式获取三方系统来源
     *
     * @return {@link TmsContractConstant.ThirdPartySystemSource}
     */
    TmsContractConstant.ThirdPartySystemSource getSystemSource();
    
    /**
     * 创建/修改三方框架合同
     *
     * @param dto      请求参数
     * @param isCreate 是否为创建
     */
    void frameworkContractCreateOrUpdate(ThirdPartyFrameworkContractDto dto, boolean isCreate);
    
    /**
     * 修改三方框架合同状态
     *
     * @param dto 请求参数
     */
    void frameworkContractModifyStatus(ThirdPartyFrameworkContractModifyStatusDto dto);
    
    /**
     * 创建/修改三方销售合同
     *
     * @param dto      请求参数
     * @param isCreate 是否为创建
     */
    void salesContractCreateOrUpdate(ThirdPartySalesContractDto dto, boolean isCreate);
    
    /**
     * 修改三方销售合同状态
     *
     * @param dto 请求参数
     */
    void salesContractModifyStatus(ThirdPartySalesContractModifyStatusDto dto);
    
    /**
     * 更新三方销售合同货物数量
     *
     * @param dto 请求参数
     */
    void salesContractUpdateGoodsQuantity(ThirdPartySalesContractGoodsQuantityDto dto);
    
    /**
     * 是否禁止重新推送三方框架合同
     *
     * @param dto 请求参数
     * @return true表示禁止推送,false表示允许
     */
    default boolean checkFrameworkContractProhibitedRepush(ThirdPartyFrameworkContractDto dto) {
        return true;
    }
    
    /**
     * 是否禁止重新推送三方销售合同
     *
     * @param dto 请求参数
     * @return true表示禁止推送,false表示允许
     */
    default boolean checkSalesContractProhibitedRepush(ThirdPartySalesContractDto dto) {
        return true;
    }
    
}
