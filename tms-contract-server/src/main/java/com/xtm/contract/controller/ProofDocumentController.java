package com.xtm.contract.controller;

import com.alibaba.fastjson.JSON;
import com.xtm.common.model.Result;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.model.vo.contract.ProofDocumentVo;
import com.xtm.contract.service.ProofDocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @package: com.xiaoniu.contract.controller.ProofDocumentController
 * @author: wwh
 * @create: 2024-12-06 15:31
 * @description: 协议相关接口-只签署，不包含业务
 **/
@Slf4j
@RestController
@RequestMapping(value = "/proofDocument")
@Api(tags = "证明协议相关接口")
public class ProofDocumentController {
    @Autowired
    private ProofDocumentService proofDocumentService;

    /**
     * 使用thirdpary替换
     * @param proofDocumentVo
     * @param result
     * @return
     * @throws Exception
     */
//    @PostMapping(value = "/sign")
//    @ApiOperation(value = "签署操作,支持一次签署多个章")
//    public Result<?> sign(@Validated @RequestBody ProofDocumentVo proofDocumentVo, BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            return Result.error(ResultCode.VALIDATOR.getCode(), Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
//        }
//        log.info("====> 合同创建修改 - 入参proofDocumentVo: {} <====", JSON.toJSONString(proofDocumentVo));
//        return Result.of(proofDocumentService.proofDocumentSign(proofDocumentVo), CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
//    }
    //使用thirdpary替换
//    @PostMapping(value = "/capacityUnitSign")
//    @ApiOperation(value = "运力单元签署操作，2次异步签署")
//    public Result<?> capacityUnitSign(@Validated @RequestBody ProofDocumentVo proofDocumentVo, BindingResult result) throws Exception {
//        if (result.hasErrors()) {
//            return Result.error(ResultCode.VALIDATOR.getCode(), Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
//        }
//        log.info("====> 运力单元合同创建修改 - 入参proofDocumentVo: {} <====", JSON.toJSONString(proofDocumentVo));
//        return Result.of(proofDocumentService.capacityUnitSign(proofDocumentVo), CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
//    }
}
