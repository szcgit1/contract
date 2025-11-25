package com.xtm.contract.model.vo.fdd.Response;

import com.xtm.contract.model.vo.fdd.Agreement;
import com.xtm.contract.model.vo.fdd.BankCard;
import com.xtm.contract.model.vo.fdd.Company;
import com.xtm.contract.model.vo.fdd.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  20:19
 *@Description:
 *@title: FindCompanyCertInfoResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindCompanyCertInfoResponse {

    private Agreement agreement;

    /**
     * 交易号
     */
    private String transactionNo;

    /**
     * 1个人
     * 2.企业
     */
    private String type;

    /**
     * yyyy-MM-dd HH:mm:ss.0
     */
    private String passTime;

    /**
     * yyyy-MM-dd HH:mm:ss.0
     */
    private String authenticationSubmitTime;

    private Company company;

    private Manager manager;

    private BankCard bankCard;
}
