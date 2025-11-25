package com.xtm.contract.model.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoOut implements Serializable {
    /**
     * id
     */
    private String fileID;
    /**
     * 上传结果
     */
    private String code;

    /**
     * 上传结果
     */
    private String message;
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件路径
     */
    private String fileServerUrl;
    /**
     * 文件来源
     */
    private String source;
    /**
     * url
     */
    private String fileUrl;
    /**
     *文字识别
     */
    private String fileJson;
    
    /**
     * 是否为草稿/暂存文件
     * 1是, 0或null否
     */
    @ApiModelProperty(value = "是否为草稿/暂存文件", notes = "1是, 0或null否")
    private Integer isDraft;

    /**
     *
     * @param code
     * @param message
     */
    public FileInfoOut(String code, String message){
        this.code = code;
        this.message = message;
    }
}
