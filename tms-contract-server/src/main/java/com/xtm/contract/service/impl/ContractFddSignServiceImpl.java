package com.xtm.contract.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.company.feign.CompanyFeign;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.service.ContractFddSignService;
import com.xtm.thirdparty.auth.model.param.FddUserBO;
import com.xtm.user.model.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/4 11:28
 * @desc
 */
@Slf4j
@Service
public class ContractFddSignServiceImpl implements ContractFddSignService {
    @Autowired
    private CompanyFeign companyFeign;
    @Autowired
    private TmsUserService userService;


    @Override
    public List<String> getOpenIds(String operatorId, Integer accountType) {
        List<String> openIds = new ArrayList<>();
        if (accountType.equals(2)){ // 企业
            Result<CompanyVo> companyInfo = companyFeign.findCompanyById(operatorId);
            if (companyInfo.isSuccess()){
                String openId = companyInfo.getData().getUnifiedSocialCreditIdentifier();
                UserInfoVo user = userService.getUserById(companyInfo.getData().getCompanyAdmin());
                String openIdU;
                if (ObjectUtil.isNotEmpty(user)) {
                    openIdU = user.getIdcardNo();
                } else {
                    openIdU = "";
                }
                openIds.add(openId);
                if (StrUtil.isNotBlank(openIdU)){
                    openIds.add(openIdU);
                }
                return openIds;
            }else {
                throw  new BusinessException(500,"公司不存在，请检查");
            }
        }else {
            UserInfoVo user = userService.getUserById(operatorId);
            if (user == null){
                throw  new BusinessException(500,"用户不存在，请检查");
            }
            if(com.xtm.utils.string.StringUtils.isBlank(user.getIdcardNo())){
                log.error("用户身份证为空，请检查:{}",user);
                return null;
            }
            openIds.add(user.getIdcardNo());
            return openIds;
        }
    }

    @Override
    public void queryOpenIdsAndUserInfo(String operatorId, Integer accountType, FddUserBO fddUserBO) {
        List<String> openIds = new ArrayList<>();
        if (accountType.equals(2)){ // 企业
            Result<CompanyVo> companyInfo = companyFeign.findCompanyById(operatorId);
            if (companyInfo.isSuccess()){
                String openId = companyInfo.getData().getUnifiedSocialCreditIdentifier();
                UserInfoVo user = userService.getUserById(companyInfo.getData().getCompanyAdmin());
                String openIdU;
                if (ObjectUtil.isNotEmpty(user)) {
                    openIdU = user.getIdcardNo();
                } else {
                    openIdU = "";
                }
                openIds.add(openId);
                if (StrUtil.isNotBlank(openIdU)){
                    openIds.add(openIdU);
                }
            }else {
                throw  new BusinessException(500,"公司不存在，请检查");
            }
        }else {
            UserInfoVo user = userService.getUserById(operatorId);
            if (user == null){
                throw  new BusinessException(500,"用户不存在，请检查");
            }
            if(com.xtm.utils.string.StringUtils.isBlank(user.getIdcardNo())){
                log.error("用户身份证为空，请检查:{}",user);
                throw new BusinessException(500,"用户身份证为空，请检查");
            }
            openIds.add(user.getIdcardNo());
            fddUserBO.setIdcardNo(user.getIdcardNo());
            fddUserBO.setUserName(user.getName());
        }
        fddUserBO.setOpenIds(openIds);
        fddUserBO.setOpenId(openIds.get(0));
        fddUserBO.setAccountType(accountType);
    }

}
