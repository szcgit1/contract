package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
	@ApiModelProperty("文件ID")
	private String fileId;

	@ApiModelProperty("文件路径")
	private String fileUrl;

	@ApiModelProperty("文件描述")
	private String fileDesc;
}
