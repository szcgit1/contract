package com.xtm.contract.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xtm.contract.model.domain.ContractCharge;
import com.xtm.contract.model.domain.SettleBills;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.PartnerFraContractVO;
import com.xtm.contract.model.vo.contract.SettleBillsInfoQryVO;
import com.xtm.contract.model.vo.contractOther.SupplementContractInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: zt
 * @Desc:  合同Mapper
 * @date: 2021/3/14 15:45
 * @version: 1.0
 */
@Mapper
public interface SettleBillsMapper extends BaseMapper<SettleBills> {
    /**
     * 查询合同列表
     * @param contractListQry
     * @return
     */
    IPage<SettleBillsInfoQryVO> findListContract(IPage<SettleBillsInfoQryVO> page, @Param("contractListQry") ContractListQryReq contractListQry);

    /**
     * 查询合同详情
     *
     * @param id
     */
    SettleBillsInfoQryVO findContractById(@Param("id") String id);

    /**
     *  查询当前合同的补充合同
     * @param contractId
     * @return
     */
    List<SupplementContractInfo> findSupplementContract(@Param("contractId") String contractId);

    /**
     * 查询费用信息
     * @return
     */
    List<ContractCharge> findContractChargeInfos(List<String> ids);

    List<PartnerFraContractVO> findPartnerContract();

    /**
     * 查询框架合同及其附件信息
     * @param contractFrameReq
     * @return
     */
    List<ContractFrameRsp> getContractFrame(ContractFrameReq contractFrameReq);
}