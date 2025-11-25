package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  17:29
 *@Description:
 *@title: Person
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    /**
     * 地址
     */
    private String address;

    /**
     * 区号
     * 中国大陆：0086,香港：00852,澳门：00853,台湾：00886,
     * 默认手机区号为中国大陆
     */
    private String areaCode;

    /**
     * 不通过原因
     */
    private String auditFailReason;

    /**
     * 审核时间  yyyy-MM-dd HH:mm:ss
     */
    private String auditorTime;

    /**
     * 身份证反面图片uuid
     * 当certType=0时是身份证反面图片uuid;
     * 当certType=1时是护照封面uuid;
     * 当certType=B时是港澳居民来往内地通行证照背面图片uuid;
     * 当certType=C时是台湾居民来往大陆通行证照封面图片uuid
     */
    private String backgroundIdCardPath;

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 出生日期  yyyy-MM-dd
     */
    private String birthday;

    /**
     * 证件类型：
     * 0：身份证；
     * 1：护照；
     * B：港澳居民来往内地通行证号;
     * C：台湾居民来往大陆通行证
     */
    private String certType;

    /**
     * 证件到期时间   yyyy-MM-dd
     */
    private String expiresDate;

    /**
     * 民族
     */
    private String fork;

    /**
     * 手势照图片uuid
     */
    private String gesturesPhotoPath;

    /**
     * 身份证正面图片uuid
     * 当certType=0时是身份证正面图片uuid;
     * 当certType=1时是护照带人像图片uuid;
     * 当certType=B时是港澳居民来往内地通行证带人像图片uuid;
     * 当certType=C时是台湾居民来往大陆通行证照带人像图片uuid
     */
    private String headPhotoPath;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 身份证是否长期有效
     * 0-否，1-是
     * 当需传身份证背面时才有值
     */
    private String isLongTerm;

    /**
     * 是否通过四要素
     * -1:未校验,0:不一致,1:一致
     */
    private String isPassFourElement;

    /**
     * 是否通过三要素
     * -1:未校验,0:不一致,1:一致
     */
    private String isPassThreeElement;

    /**
     * 颁发机构
     */
    private String issueAuthority;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 个人姓名
     */
    private String personName;

    /**
     * 腾讯云返回的照片uuid
     * 只有通过腾讯云人脸识别成功才返回
     */
    private String photoUuid;

    /**
     * 性别  1男，2女
     */
    private String sex;

    /**
     * 证件起始时间  yyyy-MM-dd
     */
    private String startDate;

    /**
     * 0:未激活；
     * 1:未认证；
     * 2:审核通过；
     * 3:已提交待审核；
     * 4:审核不通过
     */
    private String status;

    /**
     * 0：个人；
     * 1：法人；
     * 2：代理人
     */
    private String type;

    /**
     * person认证方式：
     * 0:腾讯云认证;
     * 1:三要素认证;
     * 2:手势照认证;
     * 3:四要素认证；
     * 4:蚂蚁金服认证;
     */
    private String verifyType;
}
