package com.xtm.contract.model.vo.contract;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.ProofDocumentVo
 * @author: wwh
 * @create: 2024-12-06 15:01
 * @description: 补充协议入参
 **/
@Data
public class ProofDocumentVo implements Serializable {
    private static final long serialVersionUID = 4435750181289941913L;
    //pdf文件路径
    @NotBlank(message = "pdf文件路径不能为空")
    private String pdfUrl;
    // 标题
    @NotBlank(message = "标题不能为空")
    private String title;
    // 文档id -uuid
    @NotBlank(message = "文档id不能为空")
    private String docId;
    // 法大大认证后的客户id
    @NotBlank(message = "实名认证后的客户身份证号不能为空")
    private String cardNo;
    // 签署关键字
    @NotEmpty(message = "签署关键字不能为空")
    private List<String> signKeywords;

//    @NotBlank(message = "是否结束归档，默认自动结束")
    private Boolean isOver = true;
}
