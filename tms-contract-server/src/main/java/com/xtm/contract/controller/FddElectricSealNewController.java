package com.xtm.contract.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.StatusCode;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.feign.OldContractFddElectricSealFeign;
import com.xtm.contract.feign.OldContractFeign;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.contract.model.vo.fdd.ContractReq;
import com.xtm.contract.model.vo.FddCompanyStatusVo;
import com.xtm.thirdparty.auth.model.vo.FddVerifyUrlInfoVo;
import com.xtm.contract.model.vo.fdd.Response.ContractVoRes;
import com.xtm.thirdparty.auth.model.resp.QuerySignResultResponse;
import com.xtm.contract.service.ContractService;
import com.xtm.contract.service.FddElectricSealService;
import com.xtm.common.model.Result;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.controller.FddElectricSealNewController
 * @author: wwh
 * @create: 2025-04-08 16:46
 * @description:
 **/
@Slf4j
@RestController
@RequestMapping(value = "/fddElectricSealNew")
@Api(tags = "法大大电子签章新业务相关接口-组织机构重构新增")
public class FddElectricSealNewController {

    @Autowired
    private FddElectricSealService fddElectricSealService;
    @Autowired
    private ContractService contractService;

    @Resource
    private OldContractFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    @Resource
    private OldContractFddElectricSealFeign oldFddElectricSealFeign;

    /**
     *  法大大创建用户
     * @param accountType 账号类型 1个人 2企业
     * @param operatorId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/fddCreateAccountByOpenId")
    @ApiOperation(value = "法大大创建用户")
    public Result fddCreateAccountByOpenId(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId) throws Exception{
        if (!tmsContractConfig.isContractUpdateEnable()){
            return oldFddElectricSealFeign.fddCreateAccountByOpenId(accountType,operatorId);
        }
        if (StringUtils.isBlank(operatorId) || accountType == null) {
            return Result.error( "请求参数不能为空");
        }
        log.info("法大大创建用户请求参数，账户类型为：{}，操作id为：{}",accountType,operatorId);
        fddElectricSealService.createAccountNew(accountType,operatorId);
        return Result.ok();
    }

    @GetMapping(value = "/getVerifyUrl")
    @ApiOperation(value = "获取法大大认证url")
    public Result<FddVerifyUrlInfoVo> getVerifyUrl(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId) throws Exception{

        if (StringUtils.isBlank(operatorId) || accountType == null) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        log.info("获取法大大认证url请求参数，账户类型为：{}，操作id为：{}",accountType,operatorId);
        return Result.ok(fddElectricSealService.getVerifyUrl(accountType,operatorId));
    }

    @GetMapping(value = "/findFddSealResult")
    @ApiOperation(value = "获取法大大认证结果")
    public Result<FddElectricSealResp> findFddSealResult(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId) throws Exception{
        if (!tmsContractConfig.isContractSelectEnable()){
            Result<FddElectricSealVo> fddSealResult = oldContractFeign.findFddSealResult(accountType, operatorId);
            if(fddSealResult.isSuccess()){
                FddElectricSealVo fddSealResultData = fddSealResult.getData();
                if (fddSealResultData != null){
                    FddElectricSealResp fddSealResultDataResp = new FddElectricSealResp();
                    BeanUtils.copyProperties(fddSealResultData,fddSealResultDataResp);
                    return Result.ok(fddSealResultDataResp);
                }
                return Result.ok();
            }
            return Result.error(fddSealResult.getMsg());
        }
        if (StringUtils.isBlank(operatorId) || accountType == null) {
            return Result.error( "请求参数不能为空");
        }
        log.info("获取法大大认证结果请求参数，账户类型为：{}，操作id为：{}",accountType,operatorId);
        FddElectricSealResp fddSealResult = fddElectricSealService.findFddSealResult(accountType, operatorId);
        return Result.ok(fddSealResult);
    }

    @GetMapping(value = "/fddUnbindOperator")
    @ApiOperation(value = "法大大解绑操作")
    public Result fddUnbindOperator(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId) throws Exception{
        if (!tmsContractConfig.isContractUpdateEnable()){
            return oldFddElectricSealFeign.fddUnbindOperator(accountType,operatorId);
        }
        if (StringUtils.isBlank(operatorId) || accountType == null) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        log.info("法大大解绑请求参数，账户类型为：{}，操作id为：{}",accountType,operatorId);
        if (fddElectricSealService.fddUnbindOperator(accountType,operatorId)){
            return Result.ok();
        }else {
            return Result.error(StatusCode.ERROR.getCode(), "法大大解绑操作失败");
        }
    }

    @GetMapping(value = "/fddAutoSignUrl")
    @ApiOperation(value = "获取法大大授权自动签章地址")
    public Result<FddVerifyUrlInfoVo> fddAutoSignUrl(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId){

        if (StringUtils.isBlank(operatorId) || accountType == null) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        log.info("法大大授权自动签章地址请求参数，账户类型为：{}，操作id为：{}",accountType,operatorId);
        return Result.ok(fddElectricSealService.fddAutoSignUrl(accountType,operatorId));
    }



    @PostMapping(value = "/selectAuthSignResult")
    @ApiOperation(value = "根据当前账号获取自动签授权记录")
    public Result<QuerySignResultResponse> selectAuthSignResult(@RequestParam("accountType")Integer accountType, @RequestParam("operatorId") String operatorId){
        if (StringUtils.isBlank(operatorId) || accountType == null) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        QuerySignResultResponse querySignResultResponse = fddElectricSealService.selectAuthSignResult(accountType,operatorId);
        return Result.ok(querySignResultResponse);
    }

    @GetMapping(value = "/findFddSealResultBatch")
    @ApiOperation(value = "获取法大大认证结果定时")
    public Result findFddSealResultBatch(){
        log.info("获取法大大认证结果定时开始执行。。。。");
        fddElectricSealService.findFddSealResultBatch();
        return Result.ok();
    }

    /**
     * 货主端查询合同
     * @param contractReq
     * @return
     */
    @PostMapping(value = "/findContractTemplate")
    @ApiOperation(value = "货主端查询合同")
    public Result<List<ContractVoRes>> findContractTemplate(@RequestBody ContractReq contractReq){
        if (contractReq == null || StringUtils.isBlank(contractReq.getCompanyId())) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        return Result.ok(contractService.findContractTemplate(contractReq));
    }

    @PostMapping(value = "/companyFddStatus")
    @ApiOperation(value = "公司法大大认证信息")
    public Result<List<FddCompanyStatusVo>> companyFddStatus(@RequestBody List<String> ids){
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldFddElectricSealFeign.companyFddStatus(ids);
        }
        if (CollectionUtil.isEmpty(ids)) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        return Result.ok(fddElectricSealService.queryFddCompanyStatus(ids));
    }

    @PostMapping(value = "/findIsIdCardCreateCompany")
    @ApiOperation(value = "查当前法大大企业认证是否是用身份证号注册")
    public Result<Boolean> findIsIdCardCreateCompany(@RequestParam("operatorId") String operatorId, @RequestParam("accountType")Integer accountType){
        if (StringUtils.isBlank(operatorId) || accountType == null) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "请求参数不能为空");
        }
        return Result.ok(fddElectricSealService.findIsIdCardCreateCompany(accountType,operatorId));
    }
}
