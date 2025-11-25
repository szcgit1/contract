package com.xtm.contract.model.vo.contract.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.business.purchaseAndSaleContractVo
 * @author: wwh
 * @create: 2025-05-19 15:09
 * @description:obd购销合同字段定义
 **/
@Data
@ApiModel("obd购销合同字段定义")
public class purchaseAndSaleContractVo implements Serializable {
    private static final long serialVersionUID = 5236086957228830901L;
    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String id;
    /**
     * 合同编号
     */
    @ApiModelProperty("合同编号")
    private String contractCode;

    /**
     * 签订地点
     */
    @ApiModelProperty("签订地点")
    private String signPoint;

    /**
     * 甲方名称
     */
    @ApiModelProperty("甲方名称")
    private String firstPartyName;

    /**
     * 乙方名称
     */
    @ApiModelProperty("乙方名称")
    private String secondPartyName;

    /**
     * 列表数据
     */
    @ApiModelProperty("列表数据")
    private List<ReportVo> reportList;

    /**
     * 报表合计
     */
    @ApiModelProperty("报表合计")
    private String total;

    /**
     * 报表不含税金额合计
     */
    @ApiModelProperty("报表不含税金额合计")
    private String totalNoTax;

    /**
     * 报表税额合计
     */
    @ApiModelProperty("报表税额合计")
    private String totalTax;
    /**
     * 交货时间 yyyy年mm月dd日
     */
    @ApiModelProperty("交货时间")
    private String deliveryTime;

    /**
     * 交货地点
     */
    @ApiModelProperty("交货地点")
    private String deliveryBase;

    /**
     * 续费时间 yyyy年mm月dd日
     */
    @ApiModelProperty("续费时间")
    private String renewalTime;

    /**
     * 续费金额
     */
    @ApiModelProperty("续费金额")
    private String renewalFee;

    /**
     * 甲方签订时间 yyyy年mm月dd日
     */
    @ApiModelProperty("甲方签订时间")
    private String firstPartySignTime;

    /**
     * 乙方签订时间 yyyy年mm月dd日
     */
    @ApiModelProperty("乙方签订时间")
    private String secondPartySignTime;

    @Data
    @ApiModel("合同报表列Vo")
    public static final class ReportVo implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 产品名称
         */
        @ApiModelProperty("产品名称")
        private String productName;

        /**
         * 规格型号
         */
        @ApiModelProperty("规格型号")
        private String model;


        /**
         * 数量
         */
        @ApiModelProperty("数量")
        private String count;

        /**
         * 单位
         */
        @ApiModelProperty("单位")
        private String unit;

        /**
         * 单价
         */
        @ApiModelProperty("单价")
        private String price;

        /**
         * 含税金额
         */
        @ApiModelProperty("含税金额")
        private String taxInclusiveAmount;

        /**
         * 发票税率
         */
        @ApiModelProperty("发票税率")
        private String tax;

        /**
         * 不含税金额
         */
        @ApiModelProperty("不含税金额")
        private String taxExclusiveAmount;

        /**
         * 税额
         */
        @ApiModelProperty("税额")
        private String taxAmount;

        /**
         * 备注
         */
        @ApiModelProperty("备注")
        private String remark;
    }
}
