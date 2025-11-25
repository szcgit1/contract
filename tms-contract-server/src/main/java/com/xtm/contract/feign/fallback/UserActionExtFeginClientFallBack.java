package com.xtm.contract.feign.fallback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.UserActionExtFeginClient;
import com.xtm.contract.enums.CommonLang;
import org.springframework.stereotype.Component;

/**
 * @author wanglei
 * @version 1.0
 * @date 2024/01/11 16:52
 * @desc
 */
@Component
public class UserActionExtFeginClientFallBack implements UserActionExtFeginClient {

    @Override
    public Result<Boolean> isDriverCaptain(String userId, String companyId) {
        return null;
    }

    @Override
    public Result<Boolean> checkMenuUrl(String userId, String companyId, String menuId) {
        return Result.error(CommonLang.SYS_FAIL.getCode(),"请检查xiaoniuApi服务是否正常运行");
    }

    @Override
    public Result<String> getDjDdxxAttachUrl(String id, Integer type) {
        return Result.error(CommonLang.SYS_FAIL.getCode(),"请检查xiaoniuApi服务是否正常运行");
    }

    @Override
    public Result<String> getGansuDdxxAttachUrl(String id, Integer type) {
        return Result.error(CommonLang.SYS_FAIL.getCode(),"请检查xiaoniuApi服务是否正常运行");
    }
}
