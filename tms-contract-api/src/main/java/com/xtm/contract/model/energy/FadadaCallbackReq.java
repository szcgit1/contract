package com.xtm.contract.model.energy;
/**
 * 法大大回调请求参数;
 *
 * @author miaoyouhu
 * @date 2024/5/7 17:32
 */
public class FadadaCallbackReq {

    private String documentId;//结算单据表ID;
    private Integer httpStatus;//通知状态;HTTP_STATUS 为200 即视为通知成功

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }
}