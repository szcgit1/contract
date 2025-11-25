package com.xtm.contract.controller;

import com.alibaba.fastjson.JSONObject;
import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.Constant;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.param.frameAgreement.*;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementDetailVo;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementVo;
import com.xtm.contract.service.FrameAgreementService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  13:47
 *@Description: 框架合同协议控制层
 */
@RestController
@RequestMapping("/frameAgreement")
@Slf4j
@Api(tags = "框架合同协议业务相关接口")
public class FrameAgreementController {

    @Autowired
    FrameAgreementService frameAgreementService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private NacosValueConfig nacosValueConfig;

    /**
     * 分页查询框架合同协议
     * @param queryParam
     * @return
     */
    @PostMapping("/queryPageList")
    public Result<ApiPageResult<FrameAgreementVo>> queryPageList(@RequestBody @Validated FrameAgreementParam queryParam, BindingResult bindingResult) {
        try {
            log.info("分页查询框架合同协议,入参：{}", JSONObject.toJSON(queryParam));
            if(bindingResult.hasErrors()){
                return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
            }
            return Result.ok(frameAgreementService.queryPageList(queryParam));
        } catch (BusinessException e) {
            log.error("分页查询框架合同协议发生异常,参数:{}", JSONObject.toJSON(queryParam), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("分页查询框架合同协议发生异常,参数:{}", JSONObject.toJSON(queryParam), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "分页查询框架合同协议发生失败:" + e.getMessage());
        }
    }

    /**
     * 框架合同协议详情
     * @param id
     * @return
     */
    @PostMapping("/detail/{id}")
    public Result<FrameAgreementDetailVo> queryDetailById(@PathVariable Long id) {
        try {
            log.info("框架合同协议详情,入参：{}", id);
            FrameAgreementDetailVo orderDetailVO = frameAgreementService.queryDetailById(id);
            return Result.ok(orderDetailVO);
        } catch (BusinessException e) {
            log.error("框架合同协议详情发生异常,参数:{}", id, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("框架合同协议详情发生异常,参数:{}", id, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "框架合同协议详情发生失败:" + e.getMessage());
        }
    }

    /**
     * 手动创建编辑框架合同协议
     * @return
     */
    @PostMapping("/createOrUpdate")
    public Result createOrUpdate(@RequestBody FrameAgreementSaveParam param) {
        log.info("手动创建编辑框架合同协议开始:{}", JSONObject.toJSONString(param));
        boolean tryLock = false;
        //编辑时以框架合同协议的id来加锁，同一个订单同时只有一个人能编辑
        RLock lock = redissonClient.getLock(nacosValueConfig.getPrefix()+ Constant.SAVE_UPDATE_FRAME_AGREEMENT_LOCK_KEY + param.getId());
        try {
            tryLock = lock.tryLock();
            if (!tryLock) {
                return Result.error(ErrorCodeEnum.ERROR.getCode(), "保存框架合同协议中，请稍后重试");
            }
            frameAgreementService.createOrUpdate(param);
            return Result.ok();
        } catch (BusinessException e) {
            log.error("手动创建框架合同协议发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("手动创建框架合同协议发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "手动创建编辑框架合同协议失败:" + e.getMessage());
        } finally {
            if (tryLock && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 校验手动修改合同状态
     */
    @PostMapping("/checkUpdateContractState")
    public Result checkUpdateContractState(@RequestBody @Validated FrameAgreementUpdateStateParam params) {
        try {
            log.info("校验手动修改合同状态，param：{}", JSONObject.toJSONString(params));
            frameAgreementService.checkUpdateContractState(params);
            return Result.ok();
        } catch (BusinessException e) {
            log.error("校验手动修改合同状态发生异常,参数:{}", JSONObject.toJSONString(params), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("校验手动修改合同状态发生异常,参数:{}", JSONObject.toJSONString(params), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "校验手动修改合同状态失败:" + e.getMessage());
        }
    }

    /**
     * 手动批量修改合同状态
     */
    @PostMapping("/batchUpdateContractState")
    public Result batchUpdateContractState(@RequestBody @Validated FrameAgreementUpdateStateParam params) {
        try {
            log.info("手动批量修改合同状态，param：{}", JSONObject.toJSONString(params));
            frameAgreementService.batchUpdateContractState(params);
            return Result.ok();
        } catch (BusinessException e) {
            log.error("手动批量修改合同状态发生异常,参数:{}", JSONObject.toJSONString(params), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("手动批量修改合同状态发生异常,参数:{}", JSONObject.toJSONString(params), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "手动批量修改合同状态失败:" + e.getMessage());
        }
    }


    /**
     * 关联框架合同
     */
    @PostMapping("/relateContract")
    public Result relateContract(@RequestBody FrameAgreementRelateSaleContractParam param) {
        try {
            log.info("关联框架合同，param：{}", JSONObject.toJSONString(param));
            frameAgreementService.relateContract(param);
            return Result.ok();
        } catch (BusinessException e) {
            log.error("关联框架合同发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("关联框架合同发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "关联框架合同失败:" + e.getMessage());
        }
    }

    /**
     * 取消关联框架合同
     */
    @PostMapping("/cancelRelateContract")
    public Result cancelRelateContract(@RequestBody FrameAgreementCancelRelateSaleContractParam param) {
        try {
            log.info("合同协议取消关联框架合同，param：{}", JSONObject.toJSONString(param));
            frameAgreementService.cancelRelateContract(param);
            return Result.ok();
        } catch (BusinessException e) {
            log.error("合同协议取消关联框架合同发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("合同协议取消关联框架合同发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "合同协议取消关联框架合同失败:" + e.getMessage());
        }
    }


    /**
     * 根据合同编号查询虚拟协议为否且已启用的框架合同协议
     * @param contractCode
     * @return
     */
    @PostMapping("/queryVirtualEnableByCode")
    public Result<List<FrameAgreementVo>> queryVirtualEnableByCode(@RequestParam String contractCode) {
        try {
            log.info("根据合同编号查询虚拟协议为否且已启用的框架合同协议，contractCode：{}", contractCode);
            List<FrameAgreementVo> agreementVoList = frameAgreementService.queryVirtualEnableByCode(contractCode);
            return Result.ok(agreementVoList);
        } catch (BusinessException e) {
            log.error("根据合同编号查询虚拟协议为否且已启用的框架合同协议发生异常,参数:{}", contractCode, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("根据合同编号查询虚拟协议为否且已启用的框架合同协议发生异常,参数:{}", contractCode, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "根据合同编号查询虚拟协议为否且已启用的框架合同协议失败:" + e.getMessage());
        }
    }

    /**
     * 分页查询框架合同协议历史记录
     * @param param
     * @return
     */
    @RequestMapping("/getHistoryList")
    public Result<?> getHistoryList(@RequestBody FrameAgreementHistoryListParam param) {
        try {
            log.info("分页查询框架合同协议历史记录，param：{}", JSONObject.toJSONString(param));
            return Result.ok(frameAgreementService.getHistoryList(param));
        } catch (BusinessException e) {
            log.error("分页查询框架合同协议历史记录发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("分页查询框架合同协议历史记录发生异常,参数:{}", JSONObject.toJSONString(param), e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "分页查询框架合同协议历史记录失败:" + e.getMessage());
        }
    }

    /**
     * 查询合同协议历史记录的详情
     * @param recordId
     * @return
     */
    @RequestMapping("/getHistoryDetailById/{recordId}")
    public Result<?> detail(@PathVariable(name = "recordId", required = false) String recordId) {
        try {
            log.info("查询合同协议历史记录的详情，recordId：{}", recordId);
            if (recordId == null) {
                return Result.error(CommonLang.PARAM_VALID_FAIL.getCode(), "id参数不能空");
            }
            return Result.ok(frameAgreementService.getHistoryDetailById(recordId));
        } catch (BusinessException e) {
            log.error("查询合同协议历史记录的详情发生异常,参数:{}", recordId, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("查询合同协议历史记录的详情发生异常,参数:{}", recordId, e);
            return Result.error(ErrorCodeEnum.ERROR.getCode(), "查询合同协议历史记录的详情失败:" + e.getMessage());
        }
    }

}
