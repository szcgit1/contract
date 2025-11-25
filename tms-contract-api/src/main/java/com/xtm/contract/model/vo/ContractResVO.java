package com.xtm.contract.model.vo;

import lombok.Data;

/**
  * @description:
  * @author: wjw
  * @date: 2023/10/25 11:55
  * @version: 1.0 
  */
@Data
public class ContractResVO {

    /**
     * 签章状态
     */
    private Boolean status;
    /**
    * 签章文件地址
    */
    private String pdfUrl;

}