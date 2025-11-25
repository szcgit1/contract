package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EDocsInfoRes {
    /**文档id*/
    private String fileId;
    /**文档名称*/
    private String fileName;
    /**文档地址, 有效时间1小时；该链接建议只用于下载，不要直接预览*/
    private String fileUrl;
}
