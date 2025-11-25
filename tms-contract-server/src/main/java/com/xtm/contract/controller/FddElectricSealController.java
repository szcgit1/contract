package com.xtm.contract.controller;

import cn.hutool.core.util.StrUtil;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.StatusCode;
import com.xtm.contract.feign.OldContractFddElectricSealFeign;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.fdd.DriverInfoVO;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.contract.model.vo.fdd.FddUnbindVo;
import com.xtm.contract.service.FddElectricSealService;
import com.xtm.thirdparty.auth.model.resp.AuthAutoSignResponse;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.GetVerifyUrlResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  13:36
 *@Description: 法大大电子签章业务相关接口
 *@title: FddElectricSealController
 */
@Slf4j
@RestController
@RequestMapping(value = "/fddElectricSeal")
@Api(tags = "法大大电子签章业务相关接口")
public class FddElectricSealController {

    @Autowired
    private FddElectricSealService fddElectricSealService;

    @Resource
    private OldContractFddElectricSealFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    @PostMapping(value = "/queryFddVerifyAuthStatusByOpenId")
    @ApiOperation(value = "根据当前账号查询法大大认证授权状态")
    @Deprecated
    public Result  queryFddVerifyAuthStatusByOpenId() throws Exception {
        // 获取当前登陆人信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        FddElectricSealResp fddElectricSeal = fddElectricSealService.queryFddVerifyAuthStatusByOpenId(sessionInfo.getId());
        return Result.of(fddElectricSeal, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/queryFddVerifyAuthStatusByUserId/{userId}")
    @ApiOperation(value = "根据当前userId查询法大大认证授权状态")
    public Result  queryFddVerifyAuthStatusByUserId(@PathVariable(value="userId") String userId) throws Exception {
        if(!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.queryFddVerifyAuthStatusByUserId(userId);
        }
        // 获取当前登陆人信息
        FddElectricSealResp fddElectricSeal = fddElectricSealService.queryFddVerifyAuthStatusByOpenId(userId);
        return Result.of(fddElectricSeal, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/queryFddVerifyAuthStatusByUserIds")
    @ApiOperation(value = "根据当前userId查询法大大认证授权状态")
    @Deprecated
    public Result<List<FddElectricSealVo>>  queryFddVerifyAuthStatusByUserIds(@RequestBody List<String> userIds) throws Exception {
        if(!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.queryFddVerifyAuthStatusByUserIds(userIds);
        }
        // 获取当前登陆人信息
        List<FddElectricSealVo> fddElectricSealList = fddElectricSealService.queryFddVerifyAuthStatusByOpenIds(userIds);
        return Result.of(fddElectricSealList, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/queryFddVerifyAuthStatusByDriverId/{driverId}")
    @ApiOperation(value = "根据当前驾驶员id查询法大大认证授权状态")
    @Deprecated
    public Result  queryFddVerifyAuthStatusByDriverId(@PathVariable("driverId") String driverId) throws Exception {
        // 获取当前登陆人信息
        FddElectricSealResp fddElectricSeal = fddElectricSealService.queryFddVerifyAuthStatusByDriverId(driverId);
        return Result.of(fddElectricSeal, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/selectFddSealByOpenId")
    @ApiOperation(value = "根据当前账号查询法大大签章信息-已废弃")
    @Deprecated
    public Result<FddElectricSealResp>  selectFddSealByOpenId() throws Exception {
        // 获取当前登陆人信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        FddElectricSealResp fddElectricSeal = fddElectricSealService.selectFddSealByOpenId(sessionInfo);

        return Result.of(fddElectricSeal, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/getVerifyUrlByOpenId")
    @ApiOperation(value = "根据当前账号获取法大大认证地址-已废弃，后面删除")
    @Deprecated
    public Result  getVerifyUrlByOpenId() throws Exception {
        // 获取当前登陆人信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        GetVerifyUrlResponse getVerifyUrlResponse = fddElectricSealService.getVerifyUrlByOpenId(sessionInfo);

        if("0".equals(getVerifyUrlResponse.getIdcardNo())){
            return Result.error(500,"当前登陆人的身份证信息为空，请填写身份证！");
        }
        return Result.of(getVerifyUrlResponse, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/beforeAuthsignByOpenId")
    @ApiOperation(value = "根据当前账号获取法大大授权自动签章地址-已废弃，后面删除")
    public Result  beforeAuthsignByOpenId() throws Exception {
        // 获取当前登陆人信息
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        AuthAutoSignResponse beforeAuthsignResponse = fddElectricSealService.beforeAuthsignByOpenId(sessionInfo);
        return Result.of(beforeAuthsignResponse, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    /**
     * @see #updateAutoSignStatus()
     */
    @PostMapping(value = "/updateAutoSignStatusWeb")
    @ApiOperation(value = "web端，数据-数据处理 小工具，更新法大大自动授权状态- 业务已修改")
    public Result updateAutoSignStatusByWeb(@RequestBody DriverInfoVO driverInfoVO) {
        try {
            this.fddElectricSealService.updateAutoSignStatusByWeb(driverInfoVO.getIdCard());
        } catch (BusinessException e) {
            return Result.of(null,CommonLang.OPERATE_FAIL.getCode(), e.getMessage()); // 返回给前端
        } catch (Exception e) {
            log.error("更新法大大自动授权状异常",e);
            return Result.of(null,CommonLang.OPERATE_FAIL.getCode(), e.getMessage());
        }
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    /**
     * 查询所有服务商以及认证信息
     */
    @GetMapping(value = "/queryLogisticsCompanyAndFddVerifyAuthStatus")
    public Result<List<CompanyFddVO>> queryLogisticsCompanyAndFddVerifyAuthStatus() {
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.queryLogisticsCompanyAndFddVerifyAuthStatus();
        }
        List<CompanyFddVO> companyList = fddElectricSealService.queryLogisticsCompanyAndFddVerifyAuthStatus();
        return Result.of(companyList, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }


    /**
     * 删除法大大合同文件
     */
    @PostMapping(value = "/fddContractDelete")
    public void deleteFddContract(@RequestBody(required = false) String param) {

        log.info("删除法大大合同文件开始:{}",param);
        fddElectricSealService.contractDelete(param);
        log.info("删除法大大合同文件结束:{}",param);

    }

    /**
     * 法大大解绑
     * @param fddUnbindVo
     */
    @PostMapping(value = "/fddUnbindByOpenId")
    @ApiOperation(value = "web端，数据-数据处理 小工具，法大大解绑")
    public Result  fddUnbindByOpenId(@RequestBody FddUnbindVo fddUnbindVo) throws Exception{
        String openId = fddUnbindVo.getOpenId();
        log.info("法大大解绑开始，openId：{}",openId);
        if (StrUtil.isEmpty(openId)) {
            throw new BusinessException(StatusCode.ERROR.getCode(), "身份证号不能为空");
        }
        fddElectricSealService.fddUnbind(openId);
        log.info("法大大解绑结束，openId：{}",openId);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

}
