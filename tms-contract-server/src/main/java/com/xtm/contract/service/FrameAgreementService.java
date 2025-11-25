package com.xtm.contract.service;

import com.xtm.common.model.Result;
import com.xtm.common.service.SuperService;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.FrameAgreement;
import com.xtm.contract.model.param.*;
import com.xtm.contract.model.param.frameAgreement.*;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementDetailVo;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementHistoryDetailVo;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementHistoryFieldVo;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementHistoryListVo;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementVo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 框架合同协议表服务接口
 */
public interface FrameAgreementService extends SuperService<FrameAgreement> {

    /**
     * 查询框架合同协议
     * @param queryParam 查询参数
     * @return
     */
    ApiPageResult<FrameAgreementVo> queryPageList(FrameAgreementParam queryParam);

    /**
     * 框架合同协议详情
     * @param id
     * @return
     */
    FrameAgreementDetailVo queryDetailById(Long id);

    /**
     * nc批量修改合同状态
     * @param params
     */
    Result ncBatchUpdateState(NcFrameAgreementUpdateStateParam params);

    /**
     * 校验手动修改合同状态
     * @param params
     */
    void checkUpdateContractState(FrameAgreementUpdateStateParam params);

    /**
     * 手动批量修改合同状态
     * @param params
     */
    void batchUpdateContractState(FrameAgreementUpdateStateParam params);

    /**
     * nc创建编辑框架合同协议
     * @param param
     */
    Result ncCreateOrUpdateAgreement(NcFrameAgreementSaveParam param);

    /**
     * 手动创建框架合同协议
     * @param param
     */
    Result createOrUpdate(FrameAgreementSaveParam param);

    /**
     * 根据合同编号查询虚拟协议为否且已启用的框架合同协议
     * @param contractCode
     * @return
     */
    List<FrameAgreementVo> queryVirtualEnableByCode(String contractCode);


    /**
     * 关联销售合同
     * @param param
     */
    void relateContract(FrameAgreementRelateSaleContractParam param);

    /**
     * 取消关联销售合同
     * @param param
     */
    void cancelRelateContract(FrameAgreementCancelRelateSaleContractParam param);

    /**
     * 分页查询框架合同协议历史记录
     * @param param
     * @return
     */
    public ApiPageResult<FrameAgreementHistoryListVo> getHistoryList(FrameAgreementHistoryListParam param);

    /**
     * 查询合同协议历史记录的详情
     * @param recordId
     * @return
     */
    public FrameAgreementHistoryDetailVo getHistoryDetailById(String recordId);

    /**
     * 判断框架合同是否启用（供前端日志详情展示【重新创建】按钮使用）
     * @param mainId
     * @return Boolean true:启用 false:禁用或者无单据
     */
    Boolean queryDisabledByMainId(String mainId);

    /**
     * 记录操作历史记录
     * @param name
     * @param modifyTime
     * @param beforeAgreement
     * @param afterAgreement
     * @param type
     */
    void saveHistoryRecord(String name, LocalDateTime modifyTime, FrameAgreementHistoryFieldVo beforeAgreement, FrameAgreementHistoryFieldVo afterAgreement, String type);
}