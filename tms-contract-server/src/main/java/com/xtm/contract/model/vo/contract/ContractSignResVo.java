package com.xtm.contract.model.vo.contract;

import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.ContractSignResVo
 * @author: wwh
 * @create: 2025-03-28 15:34
 * @description: 技术服务费签署返回参数
 **/
@Data
public class ContractSignResVo<T> implements Serializable {
    private static final long serialVersionUID = -4353720769745498526L;
    /**
     * 业务id
     */
    private String businessId;
    /**
     * 业务编码
     */
    private String businessCode;
    /**
     * 本地pdf文件id（签署前）
     */
    private String localPdfId;

    /**
     * 本地pdf文件路径（签署前）
     */
    private String localPdfUrl;

    /**
     * 电子签章pdf文件id（签署完成）
     */
    private String ecPdfId;

    /**
     * 电子签章pdf文件路径（签署完成）
     */
    private String ecPdfUrl;

    /**
     * 点在签章手动签署 发送短信url
     */
    private String sendUrl;
    /**
     * 额外扩展字段
     */
    private T extParam;
}
