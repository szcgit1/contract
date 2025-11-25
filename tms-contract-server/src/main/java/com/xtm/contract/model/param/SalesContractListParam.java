package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SalesContractListParam {

    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private Integer pageNum;

    /**
     * 每页数据量
     */
    @NotNull(message = "每页数据量不能为空")
    private Integer pageSize;

    /**
     * 合同名称/编码
     */
    private String contractNameCode;

    /**
     * 客户统一社会信用代码
     */
    private String customerUscc;

    /**
     * 组织名称统一社会信用代码
     */
    private String orgUscc;

    /**
     * 停用标记 0: 未停用 1: 已停用
     */
    private String disabled;

    /**
     * 运费承担方 0：自担：贸易公司/基地 1：自提：客户 2：回结：三方代收代付
     */
    @ApiModelProperty(value = "运费承担方 0:自担 1:自提 2:回结")
    private List<Integer> bearCostType;

    /**
     * 是否整单计量 0:否 1:是
     */
    private String wholeMeasurement;

    /**
     * 是否海运客户 0:否 1:是
     */
    @ApiModelProperty(value = "是否海运客户 0:否 1:是", required = true, dataType = "Integer", example = "0")
    private String oceanCustomers;

    /**
     * 系统来源 0:丰南 1:中铁 2:中重 99:本平台
     */
    private List<Integer> busiSource;

    /**
     * 起运地点
     */
    private String departurePlace;

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 用户名 搜索业务员和创建人
     */
    private String userName;
}
