package com.xtm.contract.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xtm.charge.model.vo.FindCarContractResVo
 * @author: wwh
 * @create: 2024-12-26 17:21
 * @description:
 **/
@Data
public class FindCarContractResVo implements Serializable {
    private static final long serialVersionUID = 3835765739019348080L;
    /**
     * 文件id
     */
    private String fileId;
    /**
     * 签章状态: true - 归档，false-未归档
     */
    private Boolean status;
    /**
     * 签章文件地址
     */
    private String pdfUrl;
}
