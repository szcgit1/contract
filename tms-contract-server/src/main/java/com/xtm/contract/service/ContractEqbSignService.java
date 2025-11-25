package com.xtm.contract.service;

import com.xtm.common.exception.BusinessException;
import com.xtm.contract.model.query.eqbDto.EAccountInfoDTO;
import com.xtm.contract.model.query.eqbReq.EContractEcSignReq;
import com.xtm.contract.model.query.eqbReq.EFileInfoReq;
import com.xtm.contract.model.vo.eqb.ContractSignInfo;
import com.xtm.contract.model.vo.eqb.EqbConfigInfo;
import com.xtm.company.model.vo.CompanyBO;
import com.xtm.user.model.vo.UserInfoVo;

/**
 * @Author: fengyj
 * @Description: Eqb合同服务层接口
 * @Date: Create in 14:27 2022/12/17
 */
public interface ContractEqbSignService {
    /**
     * 创建账户
     * @param configInfo
     * @param companyInfo
     * @throws BusinessException
     */
    public String createAccount(EqbConfigInfo configInfo, CompanyBO companyInfo) throws BusinessException;

    /**
     * 上传pdf文件到EQB
     * @param configInfo
     * @param ecSignReq
     * @return
     * @throws BusinessException
     */
    public EFileInfoReq uploadPdfToYQB(EqbConfigInfo configInfo, EContractEcSignReq ecSignReq);
    /**
     * 双方发起签署
     * @param configInfo
     * @param contractSignInfo
     * @return
     */
    public String bothInitiationSign(EqbConfigInfo configInfo, ContractSignInfo contractSignInfo) throws BusinessException;

    /**
     * 组装企业账户创建信息
     * @param companyInfo
     * @return
     */
    EAccountInfoDTO assemblyEnterpriseAccount(EqbConfigInfo configInfo, CompanyBO companyInfo);
    /**
     * 创建公司账户
     * @param configInfo
     * @param companyInfo
     * @return
     * @throws BusinessException
     */
    String createComapnyAccount(EqbConfigInfo configInfo,CompanyBO companyInfo) throws BusinessException;

    /**
     * 创建个人账户
     * @param configInfo
     * @param userInfo
     * @return
     * @throws BusinessException
     */
    String createPersonAccount(EqbConfigInfo configInfo, UserInfoVo userInfo) throws BusinessException;

    /**
     * 创建并设置默认签章
     * @param configInfo
     * @param accountId
     * @param userInfo
     * @throws BusinessException
     */
    void setSealDefaultSignature(EqbConfigInfo configInfo,String accountId, UserInfoVo userInfo) throws BusinessException;

    /**
     * 查询个人和企业账户ID
     * @param configInfo
     * @param xnBusinessId
     * @param type
     * @return
     * @throws BusinessException
     */
    String queryAccountId(EqbConfigInfo configInfo,String xnBusinessId,int type,boolean orgFlag,String name) throws BusinessException;

    /**
     * 获取下载文件地址
     * @param eqbConfigInfo
     * @param flowId
     * @return
     * @throws BusinessException
     */
    String getDownloadDocumentUrl(EqbConfigInfo eqbConfigInfo,String flowId) throws BusinessException;
}
