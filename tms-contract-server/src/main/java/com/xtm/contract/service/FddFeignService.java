package com.xtm.contract.service;

import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.thirdparty.auth.feign.FddElectricSealFeign;
import com.xtm.thirdparty.auth.model.param.QueryFddSealByKeyParam;
import com.xtm.thirdparty.auth.model.param.QueryFddSealListParam;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class FddFeignService {
    
    @Resource
    private FddElectricSealFeign fddElectricSealFeign;
    
    public FddElectricSealResp getFddElectricSealByOpenId(String openId) {
        log.info("查询法大大电子签章开始，openId：{}",openId);
        QueryFddSealByKeyParam param = new QueryFddSealByKeyParam();
        param.setOpenId(openId);
        Result<FddElectricSealResp> fddResult = fddElectricSealFeign.getFddElectricSeal(param);
        log.info("查询法大大电子签章结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if(fddResult.isSuccess()){
            return fddResult.getData();
        }
        throw new BusinessException(fddResult.getMsg());
    }

    public List<FddElectricSealResp> getFddElectricSealByOpenIds(List<String> openIds,Integer accountType) {
        QueryFddSealListParam param = new QueryFddSealListParam();
        param.setOpenIds(openIds);
        param.setAccountType(accountType);
        log.info("查询法大大电子签章开始，参数：{}", JsonUtils.toJSONString( param));
        Result<List<FddElectricSealResp>> fddResult = fddElectricSealFeign.queryFddSeals(param);
        log.info("查询法大大电子签章结束，结果：{}", JsonUtils.toJSONString(fddResult));
        if(fddResult.isSuccess()){
            return fddResult.getData();
        }
        throw new BusinessException(fddResult.getMsg());
    }
}
