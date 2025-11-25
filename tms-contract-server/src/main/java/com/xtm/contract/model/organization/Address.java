package com.xtm.contract.model.organization;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 地址
 */
@ApiModel(value="com.xiaoniu.tms.common.model.domain.Address",description="地址")
@Data()
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class Address implements Serializable {
    @ApiModelProperty(value="ID",name="id")
    private String id;

    /**
     * 街道地址
     */
    @ApiModelProperty(value="街道地址",name="streetName")
    private String streetName;

    /**
     * 行政区划ID
     */
    @ApiModelProperty(value="行政区划ID",name="administratorDivisionId")
    private Integer administratorDivisionId;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人",name="creater", hidden = true)
    private String creater;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间",name="createTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value="修改人",name="modifier", hidden = true)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间",name="modifyTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    /**
     * 版本
     */
    @ApiModelProperty(value="版本",name="ver", hidden = true)
    private Integer ver;

    /**
     *  坐标
     */
    private String coordinate;
}