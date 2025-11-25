package com.xtm.contract.mapper;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtm.contract.model.domain.Contract;
import com.xtm.contract.model.domain.ContractCharge;
import com.xtm.contract.model.param.ContractParam;
import com.xtm.contract.model.param.ContractFrameReq;
import com.xtm.contract.model.query.contract.ContractListQryReq;
import com.xtm.contract.model.vo.ContractFrameRsp;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.model.vo.ContractVo;
import com.xtm.contract.model.vo.contractOther.SupplementContractInfo;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.fdd.ContractReq;
import com.xtm.contract.model.vo.fdd.Response.ContractVoRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: zt
 * @Desc:  合同Mapper
 * @date: 2021/3/14 15:45
 * @version: 1.0
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {
    /**
     * 查询合同列表
     * @param contractListQry
     * @return
     */
    IPage<ContractInfoQryVO> findListContract(IPage<ContractInfoQryVO> page, @Param("contractListQry") ContractListQryReq contractListQry,
                                              @Param("authCompanyIds")List<String> authCompanyIds);

    /**
     * 查询合同详情
     *
     * @param id
     */
    ContractInfoQryVO findContractById(@Param("id") String id);

    /**
     * 查询合同详情(迁移)
     *
     * @param id
     */
    ContractInfoQryVO findMigrateContractById(@Param("id") String id,@Param("suffix") String suffix);

    List<ContractInfoQryVO> findContractByParentId(@Param("pId") String pId);

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

//    List<PartnerFraContractVO> findPartnerContract();

    /**
     * 查询框架合同及其附件信息
     * @param contractFrameReq
     * @return
     */
    List<ContractFrameRsp> getContractFrame(ContractFrameReq contractFrameReq);

    /**
     * 获取一天的合同ID
     * @param startTimeDate
     * @param endTimeDate
     * @param dispatchBatch
     * @return
     */
    List<String> getContractIdsByParam(@Param("startTime") LocalDateTime startTimeDate, @Param("endTime") LocalDateTime endTimeDate, @Param("documentType") Integer dispatchBatch);

    /**
     * 查询迁移的合同详情
     *
     * @param id
     */
    Contract getMigrateContract(@Param("id") String id, @Param("suffix") String suffix);

    List<ContractVoRes> findContractTemplate(ContractReq contractReq);

    /**
     * 查询合同表数据
     * @param param
     * @return
     */
    List<ContractVo> queryContractAllData(@Param("param") ContractParam param);

    /**
     * @author 汤亚超
     * @Date 2024/11/20
     * @Desc 统计全平台交易额
     */
    BigDecimal getCumulativeTradingVolume();

    List<ContractInfoQryVO> findContractList( @Param("contractListQry") ContractListQryReq contractListQryReq, @Param("authCompanyIds") List<String> authCompanyIds);
}
