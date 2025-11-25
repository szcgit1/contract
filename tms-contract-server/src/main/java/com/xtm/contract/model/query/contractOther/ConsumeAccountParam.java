package com.xtm.contract.model.query.contractOther;

import com.xtm.contract.model.common.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 消费者账号
 * </p>
 *
 * @author 周通
 * @since 2021-07-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeAccountParam extends Page {

    @ApiModelProperty(value = "增值业务Code")
    private Integer vasCode;

    @ApiModelProperty(value = "流水相关单据Id")
    private String documentId;
}
