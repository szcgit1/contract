package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-01  14:43
 *@Description: 法大大配置信息
 *@title: FddConfigInfo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FddConfigInfo implements Serializable {

    private String host;

    private String appId;

    private String appKey;

    private String v;

    /**
     * 第一个签章位置 关键字偏移量，便宜x位置 [-595,595]之间的数字 例如:25
     */
    private String firstKeyx;

    /**
     * 第一个签章位置 关键字偏移量，便宜y位置 [-842,842]之间的数字
     */
    private String firstKeyy;

    /**
     * 第二个签章位置 关键字偏移量，便宜x位置 [-595,595]之间的数字 例如:25
     */
    private String sencondKeyx;

    /**
     * 第二个签章位置 关键字偏移量，便宜y位置 [-842,842]之间的数字
     */
    private String secondKeyy;

}
