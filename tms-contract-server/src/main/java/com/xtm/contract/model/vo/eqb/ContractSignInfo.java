package com.xtm.contract.model.vo.eqb;

import com.xtm.contract.model.query.eqbDto.ESignAccountDTO;
import lombok.Data;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/5 16:11
 * @desc
 */
@Data
public class ContractSignInfo {
    /**
     * 托运方签署信息
     */
    private ESignAccountDTO trustorEsignAccountInfo;
    /**
     * 承运方签署信息
     */
    private ESignAccountDTO carrierEsignAccountInfo;
    /**
     * 平台方签署信息
     */
    private ESignAccountDTO platEsignAccountInfo;
    /**
     * 合同业务性质
     */
    private Integer businessType;
    /**
     * 合同标题
     */
    private String title;
    /**
     * 签署文档ID
     */
    private String eqbFileId;
    /**
     * 签署文件名（带扩展名）
     */
    private String fileName;

    /**
     * 指定意愿认证方式
     */
    private String willType;
}
