package com.xtm.contract.constant;

import lombok.Data;

/**
 * @package: com.xiaoniu.contract.constant.FindCarConstantResult
 * @author: wwh
 * @create: 2024-12-27 09:02
 * @description:
 **/
@Data
public class FindCarConstantResult{
    /**
     * 错误码（11XXXX：配置类错误,12XXXX:逻辑错误,14XXXX:业务类错误）
     */
    private Integer code;

    /**
     * 错误描述
     */
    private String msg;
    /**
     *
     */
    private String data;
}
