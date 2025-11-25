package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.config.TmsContractConfig;
import com.xtm.contract.enums.DicConstant;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.feign.DocumentFeginClient;
import com.xtm.contract.feign.OldContractFeign;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.param.FrameContractVO;
import com.xtm.contract.model.query.contract.FrameContractCreUpdReq;
import com.xtm.contract.model.param.FrameContractPartnerReq;
import com.xtm.contract.model.query.contractOther.DocumentInfoQryIn;
import com.xtm.contract.model.query.contractOther.TransportChargeDetail;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.contract.FrameContractDtlQryVO;
import com.xtm.contract.model.vo.PartnerFraContractVO;
import com.xtm.contract.service.FrameworkContractService;
import com.xtm.contract.utils.EqbHelper;
import com.xtm.contract.utils.OrganizationOrSettingHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@RequestMapping(value = "frame/")
@Api(tags = "框架合同业务相关接口")
public class ContractFrameworkController {
    @Autowired
    private FrameworkContractService frameworkContractService;

    @Autowired
    private DocumentFeginClient documentFeginClient;

    @Resource
    private OldContractFeign oldContractFeign;

    @Resource
    private TmsContractConfig tmsContractConfig;

    @PostMapping(value = "/insertOrUpd")
    @ApiOperation(value = "框架合同创建修改")
    public Result  contractCreOrUpd(@RequestBody FrameContractCreUpdReq inParam) throws Exception {
        frameworkContractService.creatOrUpdFrameContract(inParam);
        return Result.of(null, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/detail/{id}")
    @ApiOperation(value = "框架合同详情查询")
    public Result<FrameContractDtlQryVO> contractList(@PathVariable("id") String id) throws Exception {
        if (!tmsContractConfig.isContractSelectEnable()){
            Result<FrameContractVO> frameContractDetail = oldContractFeign.getFrameContractDetail(id);
            if (frameContractDetail.isSuccess()){
                FrameContractVO contractVO = frameContractDetail.getData();
                FrameContractDtlQryVO contractDtlQryVO = new FrameContractDtlQryVO();
                BeanUtils.copyProperties(contractVO, contractDtlQryVO);
                return Result.of(contractDtlQryVO, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
            }
            return Result.of(null, frameContractDetail.getCode(), frameContractDetail.getMessage());
        }
        FrameContractDtlQryVO contractInfo = frameworkContractService.getFrameContractDtlById(id);
        return Result.of(contractInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/rebuildFraEcSignature")
    @ApiOperation(value = "重新生成电子签章")
    public Result<ContractPathVO> rebuildEcSignature(@ApiParam(name = "id", value = "合同id", required = true) @RequestParam String id) throws Exception {
        ContractPathVO contractPathInfo = frameworkContractService.rebuildFraEcContract(id);
        return Result.of(contractPathInfo, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @PostMapping(value = "/queryPartnerContracts")
    @ApiOperation(value = "通过伙伴公司ID查询伙伴合同列表")
    public Result<List<PartnerFraContractVO>> selectPartnerFraContractList(@RequestBody FrameContractPartnerReq partnerReq) {
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.selectPartnerFraContractList(partnerReq);
        }
        List<PartnerFraContractVO> contractList = frameworkContractService.selectPartnerFraContractList(partnerReq);
        return Result.of(contractList, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

    @GetMapping(value = "/querytransport/{documentId}")
    @ApiOperation(value = "查询费用详情")
    public Result  queryCharge(@PathVariable("documentId") String documentId) {
        DocumentInfoQryIn documentInfoQryIn = new DocumentInfoQryIn(documentId,DicConstant.DOCUMENT_TYPE.ORDER,null,null);
        TransportChargeDetail transportChargeDetail = documentFeginClient.selectTransportChargeDetail(documentInfoQryIn);
        return Result.of(transportChargeDetail, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }


    @PostMapping("/getContractFrame")
    @ApiOperation(value = "框架合同及其附件查询")
    public List<ContractFrameRsp> getContractFrame(@RequestBody ContractFrameReq contractFrameReq){
        if (!tmsContractConfig.isContractSelectEnable()){
            return oldContractFeign.getContractFrame(contractFrameReq);
        }
        return frameworkContractService.getContractFrame(contractFrameReq);
    }

    @GetMapping(value = "/getContractFile/{id}")
    @ApiOperation(value = "框架合同附件查询")
    public Result<ContractFrameRsp> getContractFile(@PathVariable("id") String id) throws Exception {
        ContractFrameRsp  result = frameworkContractService.getContractFile(id);
        return Result.of(result, CommonLang.SUCCESS.getCode(), CommonLang.SUCCESS.getMessage());
    }

}

