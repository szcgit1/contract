package com.xtm.contract.convert;

import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractDto;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractModifyStatusDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractGoodsQuantityDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractModifyStatusDto;
import com.xtm.contract.model.param.NcFrameAgreementSaveParam;
import com.xtm.contract.model.param.NcFrameAgreementUpdateStateParam;
import com.xtm.contract.model.param.NcSalesContractAddOrUpdateParam;
import com.xtm.contract.model.param.NcUpdateStateParam;
import com.xtm.contract.model.param.UpdateAccumulateOrdersMainQuantityParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Optional;

/**
 * 三方合同转换器
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-08-29 20:55
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ThirdPartyContractConverter {
    
    /**
     * 三方框架合同DTO参数转换为物流协议保存参数
     *
     * @param dto 框架合同DTO
     * @return {@link NcFrameAgreementSaveParam}
     */
    @Mappings(value = {
            @Mapping(source = "org", target = "saleOrg"),
            @Mapping(source = "orgUscc", target = "saleOrgUscc"),
            @Mapping(source = "systemSource", target = "systemSource", qualifiedByName = "systemSource2Code"),
            @Mapping(target = "virtualTag", expression = "java(Boolean.TRUE.equals(dto.getVirtualTag()) ? com.xtm.contract.constant.TmsContractConstant.ONE : com.xtm.contract.constant.TmsContractConstant.ZERO)"),
            @Mapping(source = "relatedContracts", target = "agreementSubList"),
            @Mapping(target = "disabled", constant = TmsContractConstant.ZERO_STR)
    })
    NcFrameAgreementSaveParam dto2Param(ThirdPartyFrameworkContractDto dto);
    
    /**
     * 三方框架合同修改状态DTO参数转换为物流协议更新状态参数
     *
     * @param dto 三方框架合同修改状态DTO
     * @return {@link NcFrameAgreementUpdateStateParam}
     */
    @Mappings(value = {
            @Mapping(target = "disabled", constant = TmsContractConstant.ONE_STR)
    })
    NcFrameAgreementUpdateStateParam dto2Param(ThirdPartyFrameworkContractModifyStatusDto dto);
    
    /**
     * 三方销售合同DTO参数转换为销售合同新增或更新参数
     *
     * @param dto 三方销售合同DTO
     * @return {@link NcSalesContractAddOrUpdateParam}
     */
    @Mappings(value = {
            @Mapping(source = "systemSource", target = "systemSource", qualifiedByName = "systemSource2Code"),
            @Mapping(source = "contractGoods", target = "salesContractGoods"),
            @Mapping(source = "contractTerms", target = "salesContractTerms"),
            @Mapping(source = "changes", target = "changeList")
    })
    NcSalesContractAddOrUpdateParam dto2Param(ThirdPartySalesContractDto dto);
    
    /**
     * 三方销售合同修改状态DTO转换为销售合同更新状态参数
     *
     * @param dto 三方销售合同修改状态DTO
     * @return {@link NcUpdateStateParam}
     */
    @Mappings(value = {
            @Mapping(source = "status", target = "contractStatus")
    })
    NcUpdateStateParam dto2Param(ThirdPartySalesContractModifyStatusDto dto);
    
    /**
     * 三方销售合同货物数量DTO转换为销售合同货物累计订单主数量参数
     *
     * @param dto 三方销售合同货物数量DTO
     * @return {@link UpdateAccumulateOrdersMainQuantityParam}
     */
    @Mappings(value = {
            @Mapping(source = "systemSource", target = "systemSource", qualifiedByName = "systemSource2Code"),
            @Mapping(source = "goodsQuantities", target = "goodsQuantityList")
    })
    UpdateAccumulateOrdersMainQuantityParam dto2Param(ThirdPartySalesContractGoodsQuantityDto dto);
    
    /**
     * 系统来源转换为编码
     *
     * @param systemSource 枚举
     * @return 编码
     */
    @Named(value = "systemSource2Code")
    default Integer systemSource2Code(TmsContractConstant.ThirdPartySystemSource systemSource) {
        return Optional.ofNullable(systemSource)
                .map(TmsContractConstant.ThirdPartySystemSource::getCode)
                .orElse(null);
    }
    
}
