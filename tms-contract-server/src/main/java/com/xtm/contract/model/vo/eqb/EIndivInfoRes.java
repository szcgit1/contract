package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description:    个人实名认证的基本信息
* @Author:         mxr
* @CreateDate:     2021-03-05 11:33
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EIndivInfoRes {
    /**用户id*/
    private String accountId;
    /**姓名*/
    private String name;
    /**证件号*/
    private String certNo;
    /**证件类型*/
    private String certType;
    /**国籍/地区*/
    private String nationality;
    /**手机号*/
    private String mobileNo;
    /**银行卡号*/
    private String bankCardNo;
    /**刷脸认证时刷脸照片（base64编码照片图片数据），有效期默认1个小时*/
    private String facePhotoUrl;
    /**刷脸照片相似度得分*/
    private String similarity;
    /**刷脸活体检测得分*/
    private String livingScore;
}
