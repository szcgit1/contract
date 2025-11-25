package com.xtm.contract.feign;

import com.xtm.contract.config.FeignConfig;
import com.xtm.common.model.Result;
import com.xtm.contract.feign.fallback.UserActionExtFeginClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @Author: wanglei
* @Description: 公司服务feign接口
* @Date: Create in 14:25 2022/12/17
*/
@FeignClient(value = "zhgt", configuration = FeignConfig.class, path = "/xiaoniu-niu", fallback = UserActionExtFeginClientFallBack.class)
public interface UserActionExtFeginClient {

    /**
     * 调用Niu服务的判断是否为车队长查询接口
     * @param userId 用户id
     * @param companyId 公司id
     * @return Result 
     */
    @PostMapping(value = "/api/isDriverCaptain")
    Result<Boolean> isDriverCaptain(@RequestParam(value = "userId") String userId,@RequestParam(value = "companyId") String companyId);

    /**
     * 校验用户是否含有某个菜单权限
     * @param userId 用户id
     * @param companyId 公司id
     * @param menuId 菜单id
     */
    @PostMapping(value = "/api/checkMenuUrl")
    Result<Boolean> checkMenuUrl(@RequestParam(value = "userId") String userId,@RequestParam(value = "companyId") String companyId,@RequestParam(value = "menuId") String menuId);

    /**
     * 获取无船tob 业务申报合同url
     * @param id id
     * @param type 类型 3委托方合同 4托运方合同
     */
    @GetMapping(value = "/api/getDjDdxxAttachUrl")
    Result<String> getDjDdxxAttachUrl(@RequestParam(value = "id") String id,@RequestParam(value = "type") Integer type);

    /**
     * 获取无船tob 业务申报合同url
     * @param id id
     * @param type 类型 3委托方合同 4托运方合同
     */
    @GetMapping(value = "/api/gansu/getGansuDdxxAttachUrl")
    Result<String> getGansuDdxxAttachUrl(@RequestParam(value = "id") String id,@RequestParam(value = "type") Integer type);
}
