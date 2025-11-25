package com.xtm.contract.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.feign.OldContractFeign;
import com.xtm.contract.model.mq.FinanceFddCostAuth;
import com.xtm.contract.model.mq.FinanceFddCostContract;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.contract.service.ChargeService;
import com.xtm.contract.utils.RocketMqSendUtil;
import com.xtm.thirdparty.auth.model.vo.ContractSignResVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @package: com.xiaoniu.contract.controller.ServiceChargeController
 * @author: wwh
 * @create: 2025-03-28 16:00
 * @description: 服务费通用接口，后面可以延续此接口
 **/
@Slf4j
@RestController
@RequestMapping(value = "/serviceCharge")
@Api(tags = "服务费业务相关接口")
public class ServiceChargeController {

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private RocketMqSendUtil rocketMqSendUtil;

    @Resource
    private OldContractFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    @PostMapping(value = "/serviceEcSign")
    @ApiOperation(value = "电子签章")
    public Result<ContractSignResVo> serviceEcSign(@RequestBody ContractSignVo<?> contractSignVo, BindingResult bindingResult) throws Exception {
        if(!tmsContractConfig.isContractUpdateEnable()){
            return oldContractFeign.serviceEcSign(contractSignVo);
        }
        try {
            log.info("====> 电子签章入参: {} <====", contractSignVo);
            if(bindingResult.hasErrors()){
                return Result.error(ResultCode.VALIDATOR.getCode(),bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            Result<ContractSignResVo> chargeSummaryEcSign = chargeService.serviceChargeSummaryEcSign(contractSignVo);
            log.info("====> 子签章响应：{}" , JSONObject.toJSONString(chargeSummaryEcSign));
            return chargeSummaryEcSign;
        } catch (BusinessException e) {
            log.error("电子签章失败，乙方id={}，失败原因：" , contractSignVo.getSecondPartyId(),e);
            throw new BusinessException(ResultCode.VALIDATOR.getCode(),e.getMessage());
        }catch (Exception e) {
            log.error("电子签章失败，乙方id={}，失败原因：" , contractSignVo.getSecondPartyId(),e);
            throw new Exception(e);
        }
    }

    @PostMapping(value = "/energyEcSign")
    @ApiOperation(value = "能源单电子签章")
    public Result<ContractSignResVo> energyEcSign(@RequestBody ContractSignVo<?> contractSignVo, BindingResult bindingResult) throws Exception {
        if(!tmsContractConfig.isContractUpdateEnable()){
            return oldContractFeign.energyEcSign(contractSignVo);
        }
        try {
            log.info("====> 能源单电子签章入参: {} <====", contractSignVo);
            if(bindingResult.hasErrors()){
                return Result.error(ResultCode.VALIDATOR.getCode(),bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            Result<ContractSignResVo> chargeSummaryEcSign = chargeService.energyChargeSummaryEcSign(contractSignVo);
            log.info("====> 能源单电子签章响应：{}" , JSONObject.toJSONString(chargeSummaryEcSign));
            return chargeSummaryEcSign;
        } catch (BusinessException e) {
            log.error("能源单电子签章失败，乙方id={}，失败原因：" , contractSignVo.getSecondPartyId(),e);
            throw new BusinessException(ResultCode.VALIDATOR.getCode(),e.getMessage());
        }catch (Exception e) {
            log.error("能源单单电子签章失败，乙方id={}，失败原因：" , contractSignVo.getSecondPartyId(),e);
            throw new Exception(e);
        }
    }

    @PostMapping(value = "/sendFinanceFddCostContractMsg")
    public Result<String> sendFinanceFddCostContractMsg(@RequestBody FinanceFddCostContract message){
        log.info("===================法大大自动签署合同发送消息开始========================,{}", JSONUtil.toJsonStr( message));
        rocketMqSendUtil.sendMsg(message.getTopic(), JSONObject.toJSONString(message));
        log.info("===================法大大自动签署合同发送消息结束========================");
        return Result.ok();
    }

    @PostMapping(value = "/sendFinanceFddCostAuthMsg")
    public Result<String> sendFinanceFddCostAuthMsg(@RequestBody FinanceFddCostAuth message){
        log.info("=============法大大实名认证发送信息:{} ",JSONUtil.toJsonStr( message));
        rocketMqSendUtil.sendMsg(message.getTopic(), JSONObject.toJSONString(message));
        log.info("===================法大大实名认证发送消息结束========================");
        return Result.ok();
    }
}
