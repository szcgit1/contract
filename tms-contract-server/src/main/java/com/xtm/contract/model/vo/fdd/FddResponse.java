package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-01  16:28
 *@Description:
 *@title: FddResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FddResponse<T> {

    /**
     * 状态码
     **/
    private Integer code;

    /**
     * 状态描述
     **/
    private String msg;

    /**
     * 认证序列号和编码后的认证url地址
     **/
    private T data;

}
