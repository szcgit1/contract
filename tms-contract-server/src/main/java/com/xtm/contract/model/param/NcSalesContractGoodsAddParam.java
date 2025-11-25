package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NcSalesContractGoodsAddParam {

    @ApiModelProperty(value = "合同货物id")
    private String contractGoodsId;

    @ApiModelProperty(value = "发运公司名称")
    private String shippingCompany;

    @ApiModelProperty(value = "nc发运公司主键")
    private String shippingCompanyId;

    @ApiModelProperty(value = "发运公司统一社会信用代码")
    private String shippingCompanyUscc;

    /**
     * 物料分类
     */
    @ApiModelProperty(value = "物料分类",required = false)
    private String materialClassification;

    /**
     * 基准材质
     */
    @ApiModelProperty(value = "基准材质",required = false)
    private String referenceMaterial;

    /**
     * 物料
     */
    @ApiModelProperty(value = "物料",required = false)
    private String material;

    /**
     * 材质
     */
    @ApiModelProperty(value = "材质",required = false)
    private String texture;

    /**
     * 产品线
     */
    @ApiModelProperty(value = "产品线编码")
    private String productLineCode;

    /**
     * 质量等级
     */
    @ApiModelProperty(value = "质量等级",required = false)
    private String qualityGrade;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量",required = false)
    private BigDecimal quantity;

    /**
     * 主数量
     */
    @ApiModelProperty(value = "主数量",required = false)
    private BigDecimal mainQuantity;

    /**
     * 主单位
     */
    @ApiModelProperty(value = "主单位",required = false)
    private String mainUnit;

    /**
     * 换算率
     */
    @ApiModelProperty(value = "换算率",required = false)
    private String conversionRate;

    /**
     * 基价
     */
    @ApiModelProperty(value = "基价",required = false)
    private BigDecimal basePrice;

    /**
     * 收货地点
     */
    @ApiModelProperty(value = "收货地点",required = false)
    private String receivingLocation;

    /**
     * 码头
     */
    @ApiModelProperty(value = "码头",required = false)
    private String wharf;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格",required = false)
    private String materialSpecification;

    /**
     * 主含税单价
     */
    @ApiModelProperty(value = "主含税单价",required = false)
    private BigDecimal mainTaxUnitPrice;

    /**
     * 价税合计
     */
    @ApiModelProperty(value = "价税合计",required = false)
    private BigDecimal totalPriceTax;

    /**
     * 收货国家/地区
     */
    @ApiModelProperty(value = "收货国家/地区",required = false)
    private String receivingCountry;

    /**
     * 税码
     */
    @ApiModelProperty(value = "税码",required = false)
    private String taxCode;

    /**
     * 价格组成
     */
    @ApiModelProperty(value = "价格组成",required = false)
    private String priceComposition;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注",required = false)
    private String remark;

    @ApiModelProperty(value = "主订单数量")
    private BigDecimal mainOrdersQuantity;

    @ApiModelProperty(value = "行号")
    private String lineNumber;
}
