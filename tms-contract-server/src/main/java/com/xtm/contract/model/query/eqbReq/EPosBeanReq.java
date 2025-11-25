package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

@Data
public class EPosBeanReq {
    /**页码信息*/
    private String posPage;
    /**x坐标*/
    private Float posX;
    /**y坐标*/
    private Float posY;
}
