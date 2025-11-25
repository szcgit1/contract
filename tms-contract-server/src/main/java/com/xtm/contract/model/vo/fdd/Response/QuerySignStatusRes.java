
package com.xtm.contract.model.vo.fdd.Response;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class QuerySignStatusRes {

    private String code;
    private String download_url;
    private String msg;
    private String result;
    private Integer sig_status;
    private String sign_status_desc;
    private String transaction_id;
    private String viewpdf_url;

}
