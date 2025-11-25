package com.xtm.contract.controller;

import cn.hutool.core.collection.CollUtil;
import com.xtm.common.model.Result;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.feign.OldContractFeign;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.SettleBills;
import com.xtm.contract.model.req.CommonCreUpdReq;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractResVO;
import com.xtm.contract.model.vo.contract.SettleBillsInfoQryVO;
import com.xtm.contract.service.SettleBillsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 14:40
 * @desc
 */
@Slf4j
@RestController
@RequestMapping(value = "settleBills")
@Api(tags = "结算单业务相关接口")
public class SettleBillsController {
    @Autowired
    private SettleBillsService settleBillsService;

    @Resource
    private OldContractFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    @PostMapping(value = "/insertOrUpd")
    @ApiOperation(value = "结算单创建修改")
    public Result  contractCreOrUpd(@RequestBody CommonCreUpdReq contractCreUpdParam) throws Exception {
        log.info("====> 结算单创建修改 - 入参contractCreUpdParam: {} <====", contractCreUpdParam);
        if (!tmsContractConfig.isContractUpdateEnable()){
            return oldContractFeign.contractCreOrUpd(contractCreUpdParam);
        }
        return settleBillsService.createContract(contractCreUpdParam);
    }

    @PostMapping(value = "/batchCreateSettleBills")
    @ApiOperation(value = "批量创建结算单")
    public Result  batchCreateSettleBills(@RequestBody List<CommonCreUpdReq> contractCreUpdParam) throws Exception {
        settleBillsService.batchCreateContract(contractCreUpdParam);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }


    @PostMapping(value = "/delete")
    @ApiOperation(value = "结算单删除")
    public Result  contractDelete(@ApiParam(name = "contractIds", value = "结算单id", required = true) @RequestParam List<String> contractIds) throws Exception {
        int count = settleBillsService.deleteContract(contractIds);
        if (count < 1) {
            return Result.error(500102L);
        }
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/deleteByDocument")
    @ApiOperation(value = "通过单据删除结算单")
    public Result  deleteByDocument(@RequestParam(value = "ids") List<String> ids) throws Exception {
        settleBillsService.deleteByDocument(ids);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "结算单列表查询")
    public Result<ApiPageResult<SettleBillsInfoQryVO>> contractList(@RequestBody ContractListQryReq listQryParam) throws Exception {
        ApiPageResult<SettleBillsInfoQryVO> contractListQryReq = settleBillsService.selectContractList(listQryParam);
        return Result.of(contractListQryReq, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/queryById/{id}")
    @ApiOperation(value = "结算单详情查询")
    public Result<SettleBillsInfoQryVO> contractDetail(@PathVariable("id") String id) throws Exception {
        SettleBillsInfoQryVO contractInfo = settleBillsService.selectContractDetail(id);
        return Result.of(contractInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/rebuildEcSignature")
    @ApiOperation(value = "重新生成电子签章")
    public Result<ContractPathVO> rebuildEcSignature(@ApiParam(name = "id", value = "结算单id", required = true) @RequestParam String id) throws Exception {
        ContractPathVO contractPathInfo = settleBillsService.rebuildEcContract(id);
        return Result.of(contractPathInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/queryContractCodeByDocumentId/{documentId}")
    @ApiOperation(value = "通过单据ID查询结算单编号信息")
    public Result<ContractCodeQryVO> queryContractCodeByDocumentId(@PathVariable("documentId") String documentId) {
        ContractCodeQryVO contractCodeQryVO = settleBillsService.selectContractCodeByDocumentId(documentId);
        return Result.of(contractCodeQryVO, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/rebuildLocalPdf")
    @ApiOperation(value = "重新生成本地结算单PDF")
    public Result<ContractPathVO> rebuildLocalPdf(@ApiParam(name = "id", value = "结算单id", required = true) @RequestParam String id) throws Exception {
        ContractPathVO contractPathInfo = settleBillsService.rebuildLocalPdf(id);
        return Result.of(contractPathInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/updateContractSignStatusExperid")
    @ApiOperation(value = "结算单过期提醒")
    @Deprecated //没有发现用到的地方
    public Result  updateContractSignStatusExperid() {
        log.info("CONTRACT EXPIRESJOB BIGIN");
        List<SettleBills> expiresContracts = settleBillsService.selectExpiresContract();
        if (CollUtil.isEmpty(expiresContracts)) {
            log.info("No expired contract");
            return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
        }
        log.info("The number of expired contracts" + expiresContracts.size());
        expiresContracts.forEach(contract -> settleBillsService.updateContractSignStatus(DicConstant.CONTRACT_SIGN_STATUS.EXPIRED, DicConstant.CONTRACT_SIGN_STATUS.EXPIRED, contract.getId()));
        log.info("CONTRACT EXPIRESJOB END");
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/checkSignStatus/{documentId}")
    @ApiOperation(value = "查询合同签署状态")
    public Result<ContractResVO> checkSignStatus(@PathVariable("documentId") String documentId) {
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.checkSignStatus(documentId);
        }
        ContractResVO contractCodeQryVO = settleBillsService.checkSignStatus(documentId);
        return Result.of(contractCodeQryVO, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }
    @PostMapping(value = "/gdInsertOrUpd")
    @ApiOperation(value = "创建高灯能源结算单")
    public Result  CreOrUpdGaoDengSettle(@RequestBody CommonCreUpdReq contractCreUpdParam) throws Exception {
        if (!tmsContractConfig.isContractUpdateEnable()){
            return oldContractFeign.creOrUpdGaoDengSettle(contractCreUpdParam);
        }
        log.info("====> 高灯结算单创建 - 入参contractCreUpdParam: {} <====", contractCreUpdParam);
        String gdLocalPdf = settleBillsService.createGDLocalPdf(contractCreUpdParam.getBalanceDetailRes());
        return Result.of(gdLocalPdf, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }
}

