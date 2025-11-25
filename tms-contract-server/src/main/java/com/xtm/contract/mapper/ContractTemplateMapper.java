package com.xtm.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.query.contractTemplate.ContractTemplateListQryReq;
import com.xtm.contract.model.vo.contractTemplate.ContractTemplateInfoQryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/3/16 16:34
 * @version: 1.0
 */
@Mapper
public interface ContractTemplateMapper extends BaseMapper<ContractTemplate> {
    /**
     * 合同模板列表查询
     * @param pageParam
     * @param templateListQryParam
     * @return
     */
    IPage<ContractTemplateInfoQryVO>  getContractTemplateList(Page<ContractTemplateInfoQryVO> pageParam, @Param("templateListQryParam") ContractTemplateListQryReq templateListQryParam);

    /**
     * 获取公司最新的定向模板
     * @param companyIds 公司id集合
     * @param documentType  单据类型
     * @param contractBusinessType 合同业务类型：三方、双方
     * @return
     */
    List<ContractTemplate> getLastestDirectTemplateByCompanyId(@Param("companyIds") List<String> companyIds,
                                                               @Param("documentType") Integer documentType,
                                                               @Param("contractBusinessType") Integer contractBusinessType);
}
