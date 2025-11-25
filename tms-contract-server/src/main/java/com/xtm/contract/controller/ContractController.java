package com.xtm.contract.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.ResultCode;
import com.xtm.contract.feign.OldContractFeign;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.param.ContractCreUpdReq;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.FindCarChargeSummaryPdfParam;
import com.xtm.contract.model.param.IdsEntity;
import com.xtm.contract.model.param.UpdateContractDataParam;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.query.contract.ContractPreviewReq;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.FindCarChargeSummaryPdfVo;
import com.xtm.contract.model.vo.FindCarContractResVo;
import com.xtm.contract.model.vo.ContractCodeQryVO;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 14:40
 * @desc
 */
@Slf4j
@RestController
@RequestMapping(value = "detail")
@Api(tags = "合同业务相关接口")
public class ContractController {
    @Autowired
    private ContractService contractService;

    @Resource
    private OldContractFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    /**
     * 测试分表查询
     * @param startTime 创建开始时间
     * @param endTime 创建结束时间
     */
    @GetMapping("/queryShardingTable")
    public List<Contract> listAll (String startTime,String endTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = StringUtils.isNotBlank(startTime)? LocalDateTime.parse(startTime,dateTimeFormatter):null;
        LocalDateTime end = StringUtils.isNotBlank(endTime)? LocalDateTime.parse(endTime,dateTimeFormatter):null;
        return contractService.queryShardingTable(start,end);
    }

    @PostMapping(value = "/insertOrUpd")
    @ApiOperation(value = "合同创建修改")
    public Result  contractCreOrUpd(@RequestBody ContractCreUpdReq contractCreUpdParam) throws Exception {
        log.info("====> 合同创建修改 - 入参contractCreUpdParam: {} <====", contractCreUpdParam);
        if(!tmsContractConfig.isContractUpdateEnable()){
            return oldContractFeign.insertOrUpd(contractCreUpdParam);
        }
        return contractService.createContract(contractCreUpdParam);
    }

    @PostMapping(value = "/rebuildUnionDispatchBatchContract")
    @ApiOperation(value = "重新生成联合运单合同")
    public Result  rebuildUnionDispatchBatchContract(@RequestParam("unionBatchId") String unionBatchId) {
        log.info("====> 重新生成联合运单合同 - 入参unionBatchId: {} <====", unionBatchId);
        return contractService.rebuildUnionDispatchBatchContract(unionBatchId);
    }
    @PostMapping(value = "/batchCreateContract")
    @ApiOperation(value = "批量创建合同")
    public Result  batchCreateContract(@RequestBody List<ContractCreUpdReq> contractCreUpdParam) throws Exception {
        contractService.batchCreateContract(contractCreUpdParam);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }


    @PostMapping(value = "/delete")
    @ApiOperation(value = "合同删除")
    public Result  contractDelete(@ApiParam(name = "contractIds", value = "合同id", required = true) @RequestParam List<String> contractIds) throws Exception {
        int count = contractService.deleteContract(contractIds);
        if(count < 1){
            return Result.error(500102L);
        }
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/deleteByDocument")
    @ApiOperation(value = "通过单据删除合同")
    public Result  deleteByDocument(@RequestParam(value = "ids") List<String> ids) throws Exception {
        log.info("====> 通过单据删除合同 - 入参ids: {} <====", ids);
        contractService.deleteByDocument(ids);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "合同列表查询")
    public Result<ApiPageResult<ContractInfoQryVO>> contractList(@RequestBody ContractListQryReq listQryParam) throws Exception {
        ApiPageResult<ContractInfoQryVO> contractListQryReq = contractService.selectContractList(listQryParam);
        return Result.of(contractListQryReq, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/queryById/{id}")
    @ApiOperation(value = "合同详情查询")
    public Result<ContractInfoQryVO> contractDetail(@PathVariable("id") String id) throws Exception {
        ContractInfoQryVO contractInfo = contractService.selectContractDetail(id);
        contractInfo.updateAttachStatus();
        return Result.of(contractInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/queryByDocumentId/{documentId}")
    @ApiOperation(value = "根据单据ID查询合同")
    public Result<ContractInfoQryVO> queryContractByDocumentId(@PathVariable("documentId") String documentId) throws Exception {
        ContractInfoQryVO contractInfo = contractService.selectContractByDocumentId(documentId);

        return Result.of(contractInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/rebuildEcSignature")
    @ApiOperation(value = "重新生成电子签章")
    public Result<ContractPathVO> rebuildEcSignature(@ApiParam(name = "id", value = "合同id", required = true) @RequestParam String id) throws Exception {
        ContractPathVO contractPathInfo = contractService.rebuildEcContract(id);
        contractPathInfo.setEcontractUrl(null);
        return Result.of(contractPathInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/createSupplementary")
    @ApiOperation(value = "创建补充合同")
    public Result  createSupplementary(@RequestBody ContractCreUpdReq contractCreUpdParam) throws Exception {
        contractService.createSupplementContract(contractCreUpdParam);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/queryContractCodeByDocumentId/{documentId}")
    @ApiOperation(value = "通过单据ID查询合同编号信息")
    public Result<ContractCodeQryVO> queryContractCodeByDocumentId(@PathVariable("documentId") String documentId) {
        if(!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.queryContractCodeByDocumentId(documentId);
        }
        ContractCodeQryVO contractCodeQryVO = contractService.selectContractCodeByDocumentId(documentId);
        return Result.of(contractCodeQryVO, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/rebuildLocalPdf")
    @ApiOperation(value = "重新生成本地合同PDF")
    public Result<ContractPathVO> rebuildLocalPdf(@ApiParam(name = "id", value = "合同id", required = true) @RequestParam String id) throws Exception {
        ContractPathVO contractPathInfo = contractService.rebuildLocalPdf(id);
        return Result.of(contractPathInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/updateContractSignStatusExperid")
    @ApiOperation(value = "合同过期提醒")
    public Result  updateContractSignStatusExperid(){
        log.info("CONTRACT EXPIRESJOB BIGIN");
        List<Contract> expiresContracts = contractService.selectExpiresContract();
        if(CollUtil.isEmpty(expiresContracts)){
            log.info("No expired contract");
            return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
        }
        log.info("The number of expired contracts"+expiresContracts.size());
        expiresContracts.forEach(contract -> contractService.updateContractSignStatus(DicConstant.CONTRACT_SIGN_STATUS.EXPIRED,DicConstant.CONTRACT_SIGN_STATUS.EXPIRED,contract.getId()));
        log.info("CONTRACT EXPIRESJOB END");
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/updateSettlePriceByDocumentId")
    @ApiOperation(value = "根据订单id修改合同的结算金额")
    public Result  updateSettlePriceByDocumentId(@RequestBody ContractCreUpdReq contractCreUpdReq){
        contractService.updateSettlePriceByDocumentId(contractCreUpdReq);
        return Result.of(null,CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/getContractExistByDocumentIds")
    @ApiOperation(value = "根据单据IDS查询所有存在的单据")
    public Result<List<String>> getContractExistByDocumentIds(@RequestBody IdsEntity documentIds){
        if(documentIds == null || CollectionUtil.isEmpty(documentIds.getIds())){
            return Result.error(CommonLang.PARAM_VALID_FAIL.getCode(),"参数不能为空");
        }
        return Result.of(contractService.getContractExistByDocumentIds(documentIds.getIds()),CommonLang.SUCCESS.getCode(),"success");
    }

    @PostMapping(value = "/findCarChargeSummaryEcSign")
    @ApiOperation(value = "找车费用汇总单电子签章")
    public Result<FindCarChargeSummaryPdfVo> findCarChargeSummaryEcSign(@RequestBody FindCarChargeSummaryPdfParam summaryPdfParam) throws Exception {
        if(!tmsContractConfig.isContractUpdateEnable()){
            return contractService.findCarChargeSummaryEcSign(summaryPdfParam);
        }
        try {
            log.info("====> 找车费用汇总单电子签章入参: {} <====", summaryPdfParam);
            Result<FindCarChargeSummaryPdfVo> carChargeSummaryEcSign = contractService.findCarChargeSummaryEcSign(summaryPdfParam);
            log.info("====> 找车费用汇总单电子签章响应：{}" ,JSONObject.toJSONString(carChargeSummaryEcSign));
            return carChargeSummaryEcSign;
        } catch (BusinessException e) {
            log.error("找车费用汇总单电子签章失败，乙方id={}，失败原因：" , summaryPdfParam.getSecondPartyId(),e);
            throw new BusinessException(ResultCode.VALIDATOR.getCode(),e.getMessage());
        }catch (Exception e) {
            log.error("找车费用汇总单电子签章失败，乙方id={}，失败原因：" , summaryPdfParam.getSecondPartyId(),e);
            throw new Exception(e);
        }
    }

    /**
     * 预览合同接口,返回文件流
     * @param contractPreviewReq 合同的id和类型
     * @param response
     * @return
     */
    @PostMapping(value = "/contractPreview")
    public ResponseEntity<byte[]> contractPreview(@RequestBody ContractPreviewReq contractPreviewReq, HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        String contrractId = contractPreviewReq.getContractId();
        if(StringUtils.isEmpty(contrractId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("合同id不能为空".getBytes(StandardCharsets.UTF_8));
        }
        Integer type = contractPreviewReq.getType();
        if(type == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("预览类型不能为空".getBytes(StandardCharsets.UTF_8));
        }
        return contractService.contractPreview(contractPreviewReq,request,response);
    }

    /**
     * @Param: [java.lang.String]
     * @return: com.xtm.common.model.Result<com.xiaoniu.contract.model.vo.contract.FindCarContractResVo>
     * @Author: wwh
     * @Date: 2024/12/26 17:04
     * @Description: 找车服务费签署结果查询
     */
    @GetMapping(value = "/checkSignStatus/{contractId}/{companyId}")
    @ApiOperation(value = "查询合同签署状态")
    public Result<FindCarContractResVo> checkSignStatus(@PathVariable("contractId") String contractId, @PathVariable("companyId") String companyId) {
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.checkSignStatus(contractId,companyId);
        }
        return Result.of(contractService.checkSignStatus(contractId,companyId), CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    /**
     * 修改合同表数据
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/updateContractData")
    @ApiOperation(value = "修改合同表数据")
    public Result<String> updateContractData(@RequestBody UpdateContractDataParam param) throws Exception {
        log.info("====> 修改合同表数据 - 入参param：{} <====", JSONObject.toJSONString(param));
        contractService.updateContractData(param);
        return Result.ok();
    }

    /**
     * 查询合同表数据
     * @param param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/queryContractAllData")
    @ApiOperation(value = "查询合同表数据")
    public Result<List<ContractVo>> queryContractAllData(@RequestBody ContractParam param) throws Exception {
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.queryContractAllData(param);
        }
        log.info("====> 查询合同表数据 - 入参param：{}<====", JSONObject.toJSONString(param));
        List<ContractVo> contractVos = contractService.queryContractAllData(param);
        return Result.ok(contractVos);
    }

    /**
     * @author 汤亚超
     * @Date 2024/11/20
     * @Desc 统计全平台交易额
     */
    @GetMapping("/detail/getCumulativeTradingVolume")
    public Result<BigDecimal> getCumulativeTradingVolume(){
        BigDecimal cumulativeTradingVolume = contractService.getCumulativeTradingVolume();
        return Result.ok(cumulativeTradingVolume);
    }
}
