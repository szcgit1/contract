package com.xtm.contract.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-01  16:04
 *@title: FddElectricSeal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FddElectricSealVo {

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * tms登录账户  平台方自定义唯一标识
     **/
    @ApiModelProperty(value = "tms登录账户  平台方自定义唯一标识")
    private String openId;

    /**
     * 账号类型   1个人 2企业
     **/
    @ApiModelProperty(value = "账号类型   1个人 2企业")
    private String accountType;



    /**
     * 客户编号
     **/
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 合同id
     **/
    @ApiModelProperty(value = "合同id")
    private String contractId;

    /**
     * 合同标题
     **/
    @ApiModelProperty(value = "合同标题")
    private String contractTitle;

    /**
     * pdfUrl
     **/
    @ApiModelProperty(value = "pdfUrl")
    private String pdfUrl;

    /**
     * 合同标题
     **/
    @ApiModelProperty(value = "合同标题")
    private String docTitle;

    /**
     * 自动签署交易号
     **/
    @ApiModelProperty(value = "自动签署交易号")
    private String extsignAutoTransactionId;

    /**
     * 盖章关键词
     **/
    @ApiModelProperty(value = "盖章关键词")
    private String signKeyword;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 认证状态 已认证：1；未认证：0
     */
    @ApiModelProperty(value = "认证状态 已认证：1；未认证：0")
    private Integer verifyStatus;

    /**
     * 授权自动签状态 已授权：1；未授权：0
     */
    @ApiModelProperty(value = "授权自动签状态 已授权：1；未授权：0")
    private Integer authAutoSignStatus;

    /**
     * 授权自动签合同id
     */
    @ApiModelProperty(value = "授权自动签合同id")
    private String authAutoSignContractId;

}
