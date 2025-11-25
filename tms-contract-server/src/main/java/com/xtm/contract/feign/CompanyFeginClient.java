package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.fallback.CompanyFallBack;
import com.xtm.contract.model.vo.contractOther.CompanySettingVO;
import com.xtm.contract.model.vo.contractOther.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @Author: fengyj
* @Description: 公司服务feign接口
* @Date: Create in 14:25 2022/12/17
*/
@FeignClient(value = "base-company",fallback = CompanyFallBack.class)
public interface CompanyFeginClient {
    @PostMapping(value = "/apiPlat/company/api/company/setting/queryByCompanyId")
    Result<CompanySettingVO> queryCompanySetting(@RequestParam("companyId") String companyId);

    @PostMapping(value = "/apiPlat/company/api/company/setting/queryByUserId")
    Result<CompanySettingVO> queryPlatUserSetting(@RequestParam("userId") String userId);

    @PostMapping(value = "/apiPlat/company/api/user/batchQueryUser")
    Result<List<UserVO>> batchQueryUser(@RequestBody List<String> ids);
}
