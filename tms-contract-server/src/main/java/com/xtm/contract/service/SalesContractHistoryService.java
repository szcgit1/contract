package com.xtm.contract.service;

import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.dto.HistoryDTO;
import com.xtm.contract.model.param.SalesContractHistoryListParam;
import com.xtm.contract.model.vo.SalesContractHistoryListVO;
import com.xtm.contract.model.vo.SalesContractHistoryDetailVO;

import java.time.LocalDateTime;

public interface SalesContractHistoryService {

    /**
     * 分页查询物流合同协议历史记录
     */
    ApiPageResult<SalesContractHistoryListVO> getHistoryList(SalesContractHistoryListParam param);

    /**
     * 查询合同协议历史记录的详情
     */
    SalesContractHistoryDetailVO getHistoryDetailById(String recordId);

    /**
     * 记录操作历史记录
     */
    void saveHistoryRecord(String sysUserName, LocalDateTime now , HistoryDTO historyDTO);


}
