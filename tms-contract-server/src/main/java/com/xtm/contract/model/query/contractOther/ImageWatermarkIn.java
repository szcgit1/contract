package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传水印图片，入参
 */
@Data
public class ImageWatermarkIn
{

	@ApiModelProperty(value = "图片base64",required = true)
	private String base64;

	@ApiModelProperty(value = "压缩")
	private String bizCode;

	@ApiModelProperty(value = "图片名称",required = true)
	private String fileName;

	@ApiModelProperty(value = "图片路径")
	private String filePath;

	@ApiModelProperty(value = "水印文字")
	private String waterMark;

	@ApiModelProperty(value = "文件来源")
	private String source;

	@ApiModelProperty(value = "代理商code/平台code")
	private String agentCode;

	@ApiModelProperty(value = "文件路径")
	private String url;
}
