package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESignAccountDTO {
    /**签署主体账号id*/
    private String signerAccountId;
    /**创建机构账号接口返回的企业用户的orgId**/
    @ApiModelProperty(value = "创建机构账号接口返回的企业用户的orgId")
    private String authorizedAccountId;
    /**==============签署区位置=========**/
    /**页码信息*/
    private String posPage;
    /**x坐标*/
    private Float posX;
    /**y坐标*/
    private Float posY;

    /**
     * 是否自动签署
     */
    @ApiModelProperty(value = "是否自动签署")
    private boolean autoExecute;

    /**机构签约类别，当签约主体为机构时必传，值为：2*/
    @ApiModelProperty(value = "机构签约类别")
    private String actorIndentityType;
}
