package com.xtm.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.SysUser;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.query.contract.FrameContractCreUpdReq;
import com.xtm.contract.model.param.FrameContractPartnerReq;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.ContractPathVO;
import com.xtm.contract.model.vo.contract.FrameContractDtlQryVO;
import com.xtm.contract.model.vo.PartnerFraContractVO;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/25 15:57
 * @desc
 */
/**
* @Author: fengyj
* @Description: 框架合同服务层接口
* @Date: Create in 14:27 2022/12/17
*/
public interface FrameworkContractService extends IService<Contract> {
    /**
     * 维护框架合同
     */
    void creatOrUpdFrameContract(FrameContractCreUpdReq inParam) throws Exception;

    /**
     * 框架合同详情
     */
    FrameContractDtlQryVO getFrameContractDtlById(String contractId);

    /**
     * 重新创建框架电子合同
     * @param contractId
     * @return
     */
    ContractPathVO rebuildFraEcContract(String contractId) throws BusinessException;

    /**
     * 查询伙伴框架合同集合
     * @param partnerReq
     * @return
     */
    List<PartnerFraContractVO> selectPartnerFraContractList(FrameContractPartnerReq partnerReq);

    /**
     * 创建电子框架合同pdf
     * @param contractId
     * @param sessionInfo
     * @return
     */
    ContractResult createEcFrameContractPdf(String contractId, SysUser sessionInfo);

    ContractResult createLocalFramePdf(FrameContractDtlQryVO contractInfo);

    /**
     * 查询框架合同及附件信息
     * @param contractFrameReq
     * @return
     */
    List<ContractFrameRsp> getContractFrame(ContractFrameReq contractFrameReq);

    /**
     * 查询框架合同及附件信息
     * @param id
     * @return
     */
    ContractFrameRsp getContractFile(String id);
}
