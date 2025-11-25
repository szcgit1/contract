package com.xtm.contract.model.req;

import lombok.Data;

import java.util.List;

@Data
public class ContractDeleteParam {

    private List<String> documentIds;
    /**
     * 联合运单删除合同后是否重新生成合同PDF
     */
    private boolean rebuildPdf;
}
