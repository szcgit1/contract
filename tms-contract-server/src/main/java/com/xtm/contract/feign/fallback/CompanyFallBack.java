package com.xtm.contract.feign.fallback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.CompanyFeginClient;
import com.xtm.contract.model.vo.contractOther.CompanySettingVO;
import com.xtm.contract.model.vo.contractOther.UserVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/16 16:52
 * @desc
 */
@Component
public class CompanyFallBack implements CompanyFeginClient {
    @Override
    public Result<CompanySettingVO> queryCompanySetting(String companyId) {
        return null;
    }

    @Override
    public Result<CompanySettingVO> queryPlatUserSetting(String userId) {
        return null;
    }

    @Override
    public Result<List<UserVO>> batchQueryUser(List<String> ids) {
        return null;
    }


}
