package com.xtm.contract.feign;

import cn.hutool.core.collection.CollectionUtil;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.user.feign.UserFeign;
import com.xtm.user.model.dto.UserInfoDto;
import com.xtm.user.model.vo.UserInfoVo;
import com.xtm.user.model.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TmsUserService {

    @Resource
    private UserFeign userFeign;

    /**
     *  根据用户id查询用户信息
     */
    public UserInfoVo getUserById(String userId) {
        log.info("根据用户id查询用户信息:{}", userId);
        Result<UserInfoVo> userResult = userFeign.findUserById(userId);
        log.info("查询用户信息:{}", userResult);
        if (userResult.isSuccess()){
            return userResult.getData();
        }
        log.error("查询用户信息失败:{}",userResult.getMessage());
        return null;
    }

    /**
     *  更新用户签章模板
     */
    public void updatePersonalSealTemplateIdById(UserInfoDto user){
        Result<?> result = userFeign.updatePersonalSealTemplateIdById(user);
        log.info("更新用户签章模板:{}", result);
        if (!result.isSuccess()){
            log.error("更新用户签章模板失败:{}",result.getMessage());
        }
    }

    /**
     * 根据用户id查询用户信息
     */
    public List<UserInfoVo> getUserByIds(List<String> userIds) {
        Result<List<UserInfoVo>> userResult = userFeign.getUserInfoByUserIds(userIds);
        log.info("查询用户信息:{}", userResult);
        if (userResult.isSuccess()){
            List<UserInfoVo> userData = userResult.getData();
            if (CollectionUtil.isNotEmpty(userData)){
                return userData;
            }
            return new ArrayList<>();
        }
        throw new BusinessException("查询用户信息失败:"+userResult.getMessage());
    }

    /**
     *  根据公司id查询用户信息
     * @param companyId
     * @return
     */
    public UserInfoVo getUserByCompanyId(String companyId){
        log.info("根据公司id查询用户信息,参数:{}", companyId);
        Result<List<UserInfoVo>> userResult = userFeign.getUserInfoByCompanyId(companyId);
        log.info("根据公司id查询用户信息:{}", userResult);
        if (userResult.isSuccess()){
            List<UserInfoVo> userList = userResult.getData();
            if (CollectionUtil.isNotEmpty(userList)){
                return userList.get(0);
            }
            return null;
        }
        throw new BusinessException("根据公司id查询用户信息失败:"+userResult.getMessage());
    }

    public UserVo getUserByIdCardNo(String idCardNo){
        log.info("根据身份证查询用户信息,参数:{}", idCardNo);
        Result<List<UserVo>> userResult = userFeign.getUserInfoByIdCard(idCardNo);
        log.info("根据身份证查询用户信息:{}", userResult);
        if (userResult.isSuccess()){
            List<UserVo> userList = userResult.getData();
            if (CollectionUtil.isNotEmpty(userList)){
                return userList.get(0);
            }
            return null;
        }
        throw new BusinessException("根据身份证查询用户信息失败:"+userResult.getMessage());
    }
}
