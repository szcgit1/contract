package com.xtm.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xtm.contract.model.ApiPageResult;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateCreUpdReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateInfoReq;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateListQryReq;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateDtlQryVO;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateInfoQryVO;

import java.util.List;

/**
 * @author  tong
 * @date  2020/06/19 10:24
 * @desc 合同模板服务接口
 */
public interface ContractTemplateService extends IService<ContractTemplate> {
    /**
     * 合同模板列表
     */
    ApiPageResult<ContractTemplateInfoQryVO> selectContractTemplateList(ContractTemplateListQryReq templateListQryReq) throws Exception;
    /**
     * 合同模板详情
     */
    ContractTemplateDtlQryVO selectContractTempateById(String id) throws Exception;

    /**
     * 查询合同类型模板
     */
    List<ContractTemplate> selectContractTempateByConType(Integer contractType) throws Exception;

    /**
     * 合同模板详情
     */
    ContractTemplate selectContractTempateByCompany(Integer documentType, String companyId, Integer businessType);
    /**
     * 维护合同模板
     */
    String creatContractTemplate(ContractTemplateCreUpdReq inParam) throws Exception;

    /**
     * 删除合同模板
     * @param inParam
     * @throws Exception
     */
    void deleteContractTemplate(ContractTemplateInfoReq inParam) throws Exception;

    /**
     * 启用状态改变
     * @param inParam
     * @throws Exception
     */
    void enabledStatuChange(ContractTemplateInfoReq inParam) throws Exception;
}
