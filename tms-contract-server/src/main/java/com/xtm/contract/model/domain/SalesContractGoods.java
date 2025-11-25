package com.xtm.contract.model.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xtm.v1.common.model.TransitionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 物流合同货物表
 * </p>
 *
 * @author 孙志超
 * @since 2025-09-10
 */
@Data
@TableName(value ="sales_contract_goods")
@EqualsAndHashCode(callSuper = false)
public class SalesContractGoods extends TransitionEntity<SalesContractGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * nc货物表主键
     */
    private String contractGoodsId;

    /**
     * 物流合同id
     */
    private Long salesContractId;

    /**
     * 行号
     */
    private String lineNumber;

    /**
     * 基础信息-发运公司id
     */
    private String shippingCompanyId;

    /**
     * 发运公司
     */
    private String shippingCompanyName;

    /**
     * nc发运公司主键
     */
    private String ncShippingCompanyId;

    /**
     * nc发运公司名称
     */
    private String ncShippingCompany;

    @ApiModelProperty(value = "发运公司统一社会信用代码")
    private String shippingCompanyUscc;


    /**
     * 物料分类名称
     */
    private String materialClassification;

    /**
     * 基准材质
     */
    private String referenceMaterial;

    /**
     * 物料
     */
    private String material;

    /**
     * 材质
     */
    private String texture;

    /**
     * 基础信息-系统产品线id
     */
    private Long productLineId;
    /**
     * 基础信息-系统产品线id
     */
    private String productLineCode;
    /**
     * 产品线
     */
    private String productLineName;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 数量
     */
    private BigDecimal quantity;

    /**
     * 主数量
     */
    private BigDecimal mainQuantity;

    /**
     * 主单位
     */
    private String mainUnit;

    /**
     * 换算率
     */
    private String conversionRate;

    /**
     * 基价
     */
    private BigDecimal basePrice;

    /**
     * 收货地点
     */
    private String receivingLocation;

    /**
     * 码头
     */
    private String wharf;

    /**
     * 物料规格
     */
    private String materialSpecification;

    /**
     * 主含税单价
     */
    private BigDecimal mainTaxUnitPrice;

    /**
     * 价税合计
     */
    private BigDecimal totalPriceTax;

    /**
     * 收货国家地区
     */
    private String receivingCountry;

    /**
     * 税码
     */
    private String taxCode;

    /**
     * 价格组成
     */
    private String priceComposition;

    /**
     * 备注
     */
    private String remark;

    /**
     * 累计订单主数量
     */
    private BigDecimal mainOrdersQuantity;

    /**
     * 数据版本
     */
    private Integer version;
}
