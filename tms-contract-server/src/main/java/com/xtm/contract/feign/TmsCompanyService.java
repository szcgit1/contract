package com.xtm.contract.feign;

import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.company.feign.CompanyFeign;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.company.model.vo.CompanyVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TmsCompanyService {

    @Resource
    private CompanyFeign companyFeign;

    /**
     * 根据公司id查询公司信息
     */
    public CompanyBO getCompanyById(String companyId) {
        log.info("获取公司信息:{}",companyId);
        Result<CompanyBO> companyResult = companyFeign.getCompanyBOById(companyId);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            return companyResult.getData();
        }
        log.info("获取公司信息失败:{}",companyResult);
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }

    /**
     * 根据公司id查询公司信息
     * @param companyIds
     */
    public List<CompanyVo> getCompanyByIds(List<String> companyIds) {
        log.info("获取公司信息:{}",companyIds);
        Result<List<CompanyVo>> companyResult = companyFeign.findCompanysByIds(companyIds);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            return companyResult.getData();
        }
        log.info("获取公司信息失败:{}",companyResult);
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }

    /**
     *  根据公司id查询公司信息
     */
    public CompanyVo findCompanyById(String companyId) {
        log.info("获取公司信息:{}",companyId);
        Result<CompanyVo> companyResult = companyFeign.findSimpCompanyById(companyId);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            return companyResult.getData();
        }
        log.info("获取公司信息失败:{}",companyResult);
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }

    /**
     *  根据公司id查询公司信息
     */
    public List<CompanyVo> findCompanyByIds(List<String> companyIds) {
        log.info("获取公司信息:{}",companyIds);
        Result<List<CompanyVo>> companyResult = companyFeign.findCompanysByIds(companyIds);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            return companyResult.getData();
        }
        log.info("获取公司信息失败:{}",companyResult);
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }

    /**
     *  根据公司名称查询公司信息
     */
    public CompanyVo findCompanyByName(String companyName) {
        log.info("获取公司信息:{}",companyName);
        Result<List<CompanyVo>> companyResult = companyFeign.findCompanyByName(companyName);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            List<CompanyVo> companyVoList = companyResult.getData();
            for (CompanyVo companyVo : companyVoList) {
                String name = companyVo.getName();
                if (name.equals(companyName)){
                    return companyVo;
                }
            }
            return null;
        }
        log.info("获取公司信息失败:{}",companyResult);
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }

    /**
     * 查询所有物流商和管理员信息
     */
    public List<CompanyVo> getLogisticsCompanys(String compantAgentId){
        log.info("获取公司信息:{}",compantAgentId);
        Result<List<CompanyVo>> companyResult = companyFeign.getCompanyByAgentId(compantAgentId);
        log.info("获取公司信息:{}",companyResult);
        if(companyResult.isSuccess()){
            List<CompanyVo> resultData = companyResult.getData();
            if (resultData != null){
                return resultData;
            }
            return new ArrayList<>();
        }
        log.error("获取公司信息失败:{}",companyResult.getMessage());
        throw new BusinessException("获取公司信息失败:"+companyResult.getMessage());
    }



}
