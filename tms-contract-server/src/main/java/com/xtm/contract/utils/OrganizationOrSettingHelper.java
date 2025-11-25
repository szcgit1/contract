package com.xtm.contract.utils;

import cn.hutool.core.util.StrUtil;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.feign.CompanyFeginClient;
import com.xtm.contract.model.vo.contractOther.CompanySettingVO;
import com.xtm.contract.model.vo.contractOther.UserVO;
import com.xtm.company.model.vo.CompanyBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/23 11:58
 * @desc
 */
@Slf4j
@Component
public class OrganizationOrSettingHelper {
    /**
     * 获取公司或个人管理员关键字
     * @param companyBO
     * @return
     */
    public String getKeyWord(CompanyBO companyBO) {
        if (DicConstant.ORGANIZATION_TYPE.LEGAL_PERSON.equals(companyBO.getOrganizationType()==null?null:companyBO.getOrganizationType().intValue())) {
            return companyBO.getId();
        }
        if (StrUtil.isNotBlank(companyBO.getCompanyAdmin())) {
            return companyBO.getCompanyAdmin();
        }
        return null;
    }

}
