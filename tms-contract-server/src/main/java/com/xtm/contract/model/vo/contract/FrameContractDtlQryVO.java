package com.xtm.contract.model.vo.contract;

import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.vo.FileInfo;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.user.model.vo.UserInfoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: zt
 * @Desc:   框架合同vo
 * @date: 2021/6/27 17:33
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameContractDtlQryVO extends Contract {
    /**
     * 委托人
     */
    @ApiModelProperty("委托人公司信息")
    private CompanyBO trustorCompany;
    /**
     *承运人
     */
    @ApiModelProperty("承运人公司信息")
    private CompanyBO carryCompany;
    /**
     * 附件列表
     */
    @ApiModelProperty("合同附件列表")
    private List<FileInfo> attachFileInfo;
    /**
     * 编制方
     */
    @ApiModelProperty("编制方公司信息")
    private CompanyVo compileSideCompany;

    @ApiModelProperty(value = "电子合同URL（未盖章）")
    private String ecContractPathUrl;

    @ApiModelProperty(value = "电子合同URL（盖章）")
    private String ecContractPdfUrl;
    /**
     * 创建者
     */
    @ApiModelProperty("创建人信息")
    private UserInfoVo userInfo;

    @ApiModelProperty("按钮权限")
    private Map<String,String> buttonPermission;
}
