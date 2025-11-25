package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateCreUpdReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateInfoReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateListQryReq;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateCreUpdVO;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateDtlQryVO;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateInfoQryVO;
import com.xtm.contract.service.ContractTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/6/25 17:19
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "template")
@Api(tags = "合同模版业务相关接口")
public class ContractTemplateController {
    @Autowired
    private ContractTemplateService contractTemplateService;

    /**
     * 合同模板查询
     * @return
     * @throws Exception
     */
    @PostMapping("/listQry")
    @ApiOperation(value = "合同模版列表查询")
    public Result<ApiPageResult<ContractTemplateInfoQryVO>> contractTemplateListQry(@RequestBody ContractTemplateListQryReq templateListQryReq) throws Exception {
        ApiPageResult<ContractTemplateInfoQryVO> outParam = contractTemplateService.selectContractTemplateList(templateListQryReq);
        return Result.of(outParam, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping("/qry/{id}")
    @ApiOperation(value = "合同模版详情查询")
    public Result<ContractTemplateDtlQryVO> contractTemplateDtlQry(@PathVariable("id") String id) throws Exception {
        ContractTemplateDtlQryVO outParam=contractTemplateService.selectContractTempateById(id);
        return Result.of(outParam, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping("/qryByContractType/{contractType}")
    @ApiOperation(value = "根据合同类型查询合同模版")
    public Result<List<ContractTemplate>> contractTemplateQryByConType(@PathVariable("contractType") String contractType) throws Exception {
        List<ContractTemplate> outParam = contractTemplateService. selectContractTempateByConType(Integer.parseInt(contractType));
        return Result.of(outParam, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping("/insertOrUpd")
    @ApiOperation(value = "合同模版新增修改")
    public  Result  contractTemplateCreOrUpd(@RequestBody @Validated ContractTemplateCreUpdReq inParam, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultCode.VALIDATOR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
       String templateId = contractTemplateService.creatContractTemplate(inParam);
        return Result.of(templateId, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping("/delete")
    @ApiOperation(value = "合同模版删除")
    public  Result<ContractTemplateCreUpdVO> contractTemplateDelete(@RequestBody @Validated ContractTemplateInfoReq inParam) throws Exception {
        contractTemplateService.deleteContractTemplate(inParam);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping("/enabledStatuChange")
    @ApiOperation(value = "改变启用状态")
    public  Result<ContractTemplateDtlQryVO> templateEnabledStatuChange(@RequestBody @Validated ContractTemplateInfoReq inParam) throws Exception {
        contractTemplateService.enabledStatuChange(inParam);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }
}
