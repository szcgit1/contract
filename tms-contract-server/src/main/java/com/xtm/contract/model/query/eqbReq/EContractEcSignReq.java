package com.xtm.contract.model.query.eqbReq;

import com.xtm.company.model.vo.CompanyBO;
import com.xtm.company.model.vo.CompanyVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/11 16:26
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EContractEcSignReq {

    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 合同ID
     */
    private Integer contractType;

    /**
     * 托运人公司
     */
    private CompanyBO trustorCompany;

    /**
     * 承运人公司
     */
    private CompanyBO carryCompany;

    /**
     * 平台公司
     */
    private CompanyBO platCompany;

    /**
     * 合同业务性质
     */
    private Integer businessType;

    /**
     * 编制方
     */
    private CompanyVo compileSideCompany;

    /**
     * 本地pdf地址
     */
    private String localPdfUrl;

    /**
     * 合同主题
     */
    private String title;

    /**
     * eqb文件ID
     */
    private String ecContractEsignFileId;
}
