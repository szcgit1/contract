package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

import java.util.List;
@Data
public class ERequestParamReq {
    /**待签文档信息*/
    private List<EFileInfoReq> docs;
    /**流程基本信息*/
    private EFlowInfoReq flowInfo;
    private List<ESignersReq> signers;
}
