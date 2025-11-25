package com.xtm.contract.model.param;

import com.xtm.contract.model.organization.Address;
import com.xtm.contract.model.organization.Contact;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author : lushuai
 * @date :  2021/6/4 10:45
 * @description : 收发货联系人入参
 */
@Data
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Accessors(chain = true)
public class ContactParam extends Contact {

    /**
     * 地址
     */
    @ApiModelProperty("地址信息")
    private Address address;

    /**
     *  公司 Id
     */
    @ApiModelProperty("公司 Id")
    private String companyId;

    /**
     *  联系类型 (收 / 发)
     */
    @ApiModelProperty("联系类型 (收 / 发)")
    private Integer contactType;

    /**
     *  社会统一信用代码
     */
    @ApiModelProperty("社会统一信用代码")
    private String unifiedSocialCreditIdentifier;

    @ApiModelProperty("变换时间(收 / 发)")
    private Date conversionTime;
}
