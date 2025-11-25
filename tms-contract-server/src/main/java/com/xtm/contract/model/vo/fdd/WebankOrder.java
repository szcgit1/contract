package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  17:48
 *@Description:
 *@title: WebankOrder
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebankOrder {

    /**
     * 刷脸订单号
     */
    private String orderNo;

    /**
     * -1-未校验，
     * 0-不一致，
     * 1-一致
     */
    private String status;

    /**
     * 活体检测得分,整型
     */
    private String liveRate;

    /**
     * 人脸比对得分,带一位小数点
     */
    private String similarity;

}
