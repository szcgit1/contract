package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EStampTemplateRes {
    /**印章fileKey*/
    private String fileKey;
    /**印章id*/
    private String sealId;
    /**印章下载地址, 有效时间1小时*/
    private String url;
    /**印章高度, 默认95px*/
    private Integer height;
    /**印章宽度, 默认95px*/
    private Integer width;
}
