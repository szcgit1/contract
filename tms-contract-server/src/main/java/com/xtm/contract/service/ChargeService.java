package com.xtm.contract.service;

import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.contract.model.vo.ContractSignVo;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.thirdparty.auth.model.vo.ContractSignResVo;

import java.util.List;
import java.util.Map;

/**
 * @package: com.xiaoniu.contract.service.ChargeService
 * @author: wwh
 * @create: 2025-03-28 16:05
 * @description: 服务费接口
 **/
public interface ChargeService {
    /**
    * @Param:
    * @return:
    * @Author: wwh
    * @Date: 2025/4/7 13:52
    * @Description: 服务费签章流程
    */
    Result<ContractSignResVo> serviceChargeSummaryEcSign(ContractSignVo<?> contractSignVo) throws Exception;

    Result<ContractSignResVo> energyChargeSummaryEcSign(ContractSignVo<?> contractSignVo) throws Exception;

    FileInfoOut createLocalChargeSummaryPdf(ContractSignVo<?> contractSignVo, SysUser sessionInfo) throws Exception;

    /**
    * @Param:
    * @return:
    * @Author: wwh
    * @Date: 2025/4/7 11:20
    * @Description: 服务费相关文件签章接口
    */
    ContractSignResVo createEcChargeSummaryPdf(ContractSignVo<?> contractSignVo, String localPdfUrl, SysUser sessionInfo);
    /**
    * @Param:
    * @return:
    * @Author: wwh
    * @Date: 2025/4/7 11:21
    * @Description: 能源结算单文件签章接口
    */
    ContractSignResVo createEnergyEcChargeSummaryPdf(ContractSignVo<?> contractSignVo,String localPdfUrl,SysUser sessionInfo);

    /**
     *  合同查看
     * @param fddConfigInfo
     * @param contractId
     * @param sessionInfo
     * @return
     */
    FileInfoOut updateFindCarChargeEcPdfId(String contractId, SysUser sessionInfo);

    Map<String, FddElectricSealResp> getOpenIdMap(List<String> openIds);

    String getCustomerId(String openId,Integer authAutoSignStatus,Map<String, FddElectricSealResp> customerIdMap);
}
