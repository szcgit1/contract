package com.xtm.contract.model.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.domain.SalesContractGoods;
import com.xtm.contract.model.domain.SalesContractTerms;
import com.xtm.contract.model.dto.contract.SalesContractGoodsHistorySaveDTO;
import com.xtm.contract.model.dto.contract.SalesContractHistorySaveDTO;
import com.xtm.contract.model.dto.contract.SalesContractTermsHistorySaveDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HistoryDTO {

    private SalesContractHistorySaveDTO beforeSalesContract;

    private List<SalesContractGoodsHistorySaveDTO> beforeContractGoods;

    private List<SalesContractTermsHistorySaveDTO> beforeContractTerms;

    private SalesContractHistorySaveDTO afterSalesContract;

    private List<SalesContractGoodsHistorySaveDTO> afterContractGoods;

    private List<SalesContractGoods> afterContractGoodsList;

    private List<SalesContractTermsHistorySaveDTO> afterContractTerms;

    //SalesContractHistoryOperationTypeEnum
    private String operationType;

    //关联操作来源 0:NC推送关联框架合同 1:手动关联销售合同
    private Integer relateOperationSource;

    private SalesContractHistorySaveDTO before;

    private SalesContractHistorySaveDTO after;



    public void setBeforeSalesContract(SalesContract contract) {
        this.beforeSalesContract = new SalesContractHistorySaveDTO();
        BeanUtil.copyProperties(contract, beforeSalesContract);
    }

    public void setBeforeContractGoods(List<SalesContractGoods> contractGoods) {
        this.beforeContractGoods = new ArrayList<>();
        if (CollUtil.isEmpty(contractGoods)) {
            return;
        }
        contractGoods.forEach(detail -> {
            SalesContractGoodsHistorySaveDTO salesContractGoods = new SalesContractGoodsHistorySaveDTO();
            BeanUtil.copyProperties(detail, salesContractGoods);
            this.beforeContractGoods.add(salesContractGoods);
        });
    }

    public void setBeforeContractTerms(List<SalesContractTerms> contractTerms) {
        this.beforeContractTerms = new ArrayList<>();
        if (CollUtil.isEmpty(contractTerms)) {
            return;
        }
        contractTerms.forEach(detail -> {
            SalesContractTermsHistorySaveDTO salesContractTerms = new SalesContractTermsHistorySaveDTO();
            BeanUtil.copyProperties(detail, salesContractTerms);
            this.beforeContractTerms.add(salesContractTerms);
        });
    }

    public void setAfterSalesContract(SalesContract contract) {
        this.afterSalesContract = new SalesContractHistorySaveDTO();
        BeanUtil.copyProperties(contract, afterSalesContract);
    }

    public void setAfterContractGoods(List<SalesContractGoods> contractGoods) {
        this.afterContractGoodsList = contractGoods;
        this.afterContractGoods = new ArrayList<>();
        if (CollUtil.isEmpty(contractGoods)) {
            return;
        }
        contractGoods.forEach(detail -> {
            SalesContractGoodsHistorySaveDTO salesContractGoods = new SalesContractGoodsHistorySaveDTO();
            BeanUtil.copyProperties(detail, salesContractGoods);
            this.afterContractGoods.add(salesContractGoods);
        });
    }

    public void setAfterContractTerms(List<SalesContractTerms> contractTerms) {
        this.afterContractTerms = new ArrayList<>();
        if (CollUtil.isEmpty(contractTerms)) {
            return;
        }
        contractTerms.forEach(detail -> {
            SalesContractTermsHistorySaveDTO salesContractTerms = new SalesContractTermsHistorySaveDTO();
            BeanUtil.copyProperties(detail, salesContractTerms);
            this.afterContractTerms.add(salesContractTerms);
        });
    }

    public SalesContractHistorySaveDTO getAfter() {
        if (after != null){
            return after;
        }
        if (afterSalesContract == null){
            return null;
        }
        afterSalesContract.setContractGoodsHistorySaveDTOList(afterContractGoods);
        afterSalesContract.setContractTermsHistorySaveDTOS(afterContractTerms);
        return afterSalesContract;
    }

    public SalesContractHistorySaveDTO getBefore() {
        if (before != null){
            return before;
        }
        if (beforeSalesContract == null){
            return null;
        }
        beforeSalesContract.setContractGoodsHistorySaveDTOList(beforeContractGoods);
        beforeSalesContract.setContractTermsHistorySaveDTOS(beforeContractTerms);
        return beforeSalesContract;
    }
}
