package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  10:19
 *@Description: 合同签章一体化 请求参数
 *@title: SignIntegrationReq
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignIntegrationReq {


    /**
     * tms登录账户  平台方自定义唯一标识
     **/
    private String openId;

    /**
     * 账号类型   1个人 2企业
     **/
    private String accountType;

    /**
     * 客户编号 - 托运人
     **/
    private String trustorCustomerId;

    /**
     * 客户编号 - 承运人
     **/
    private String carryCustomerId;

    /**
     * 盖章关键词  托运人：
     **/
    private String trustorSignKeyword;

    /**
     * 盖章关键词  承运人：
     **/
    private String carrySignKeyword;

    /**
     * 合同id
     **/
    private String contractId;

    /**
     * 合同标题
     **/
    private String contractTitle;

    /**
     * pdfUrl
     **/
    private String pdfUrl;

    /**
     * 合同标题
     **/
    private String docTitle;

    /**
     * 自动签署交易号
     **/
    private String extsignAutoTransactionId;


    private String documentId;
    /**
     * 公司管理员电话
     */
    private String mobile;

    /**
     * 甲方签章位置关键字偏移量，便宜x位置 [-595,595]之间的数字 例如:25
     */
    private String firstKeyx;

    /**
     * 甲方签章位置关键字偏移量，便宜y位置 [-842,842]之间的数字
     */
    private String firstKeyy;

    /**
     * 乙方签章位置关键字偏移量，便宜x位置 [-595,595]之间的数字 例如:25
     */
    private String sencondKeyx;

    /**
     * 乙方签章位置关键字偏移量，便宜y位置 [-842,842]之间的数字
     */
    private String secondKeyy;
}
