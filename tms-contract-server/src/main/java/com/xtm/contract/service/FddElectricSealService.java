package com.xtm.contract.service;

import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.SysUser;
import com.xtm.contract.model.vo.CompanyFddVO;
import com.xtm.contract.model.vo.FddCompanyStatusVo;
import com.xtm.contract.model.vo.FddElectricSealVo;
import com.xtm.thirdparty.auth.model.resp.AuthAutoSignResponse;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.thirdparty.auth.model.resp.GetVerifyUrlResponse;
import com.xtm.thirdparty.auth.model.resp.QuerySignResultResponse;
import com.xtm.thirdparty.auth.model.vo.FddVerifyUrlInfoVo;

import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  11:24
 *@Description: 法大大电子签章表接口
 *@title: FddElectricSealService
 */
public interface FddElectricSealService{

    /**
     * 根据当前账号查询法大大认证授权状态（本地数据库，不调用法大大接口）
     * @param userId
     * @return
     */
    FddElectricSealResp queryFddVerifyAuthStatusByOpenId(String userId);

    /**
     * 根据当前账号查询法大大认证授权状态（本地数据库，不调用法大大接口）
     * @param userIds
     * @return
     */
    List<FddElectricSealVo> queryFddVerifyAuthStatusByOpenIds(List<String> userIds);

    /**
     * 根据当前账号查询法大大签章信息
     * @param sessionInfo
     * @return
     */
    FddElectricSealResp selectFddSealByOpenId(SysUser sessionInfo);

    /**
     * 根据当前账号获取法大大认证信息
     * @param sessionInfo
     * @return
     */
    GetVerifyUrlResponse getVerifyUrlByOpenId(SysUser sessionInfo);


    /**
     * 根据当前账号获取法大大授权地址
     * @param sessionInfo
     * @return
     */
    AuthAutoSignResponse beforeAuthsignByOpenId(SysUser sessionInfo);

    /**
     * 根据当前驾驶员id查询法大大认证授权状态
     * @param driverId
     * @return
     */
    FddElectricSealResp queryFddVerifyAuthStatusByDriverId(String driverId);

    /**
     * web端，数据-数据处理 更具司机身份证同步法大大签约状态
     * @param idCard 身份证
     */
    void updateAutoSignStatusByWeb(String idCard);

    /**
     * 查询所有物流商以及认证信息
     */
    List<CompanyFddVO> queryLogisticsCompanyAndFddVerifyAuthStatus();

    /**
     * 删除法大大合同文件
     * @param param
     */
    void contractDelete(String param);

    /**
     * 法大大解绑
     * @param openId
     */
    void fddUnbind(String openId);

    /** 以下接口为组织机构重构重构接口 **/

    /**
     * 注册账号-新
     * @param accountType 账户类型：账号类型 1个人 2企业
     * @param operatorId 平台方自定义唯一标识 ，个人userId，企业为企业id
     * @return
     * @throws BusinessException
     */
    void createAccountNew(Integer accountType , String operatorId) throws BusinessException;

    /**
     * @param accountType 账户类型：账号类型 1个人 2企业
     * @param operatorId 平台方自定义唯一标识 ，个人userId，企业为企业id
    * @return:
    * @Author: wwh
    * @Date: 2025/4/9 11:15
    * @Description: 获取法大大认证url
    */
    FddVerifyUrlInfoVo getVerifyUrl(Integer accountType , String operatorId);

    /**
     * 查询法大大授权信息结果
     * @return
     */
    FddElectricSealResp findFddSealResult(Integer accountType , String operatorId);
    /**
     * 法大大解绑
     */
    Boolean fddUnbindOperator(Integer accountType , String operatorId) throws Exception;

    /**
    * @Param:
    * @return:
    * @Author: wwh
    * @Date: 2025/4/11 11:38
    * @Description: 法大大获取认证url
    */
    FddVerifyUrlInfoVo fddAutoSignUrl(Integer accountType , String operatorId);

    /**
     * 根据当前账号获取自动签授权记录
     * @param
     * @return
     */
    QuerySignResultResponse selectAuthSignResult(Integer accountType , String operatorId);

    /**
     * 查询法大大授权信息结果定时实现
     * @param
     * @return
     */
    void findFddSealResultBatch();

    /**
     * 公司法大大认证信息
     * @param ids
     * @return
     */
    List<FddCompanyStatusVo> queryFddCompanyStatus(List<String> ids);

    /**
     * 查当前法大大企业认证是否是用身份证号注册
     * @param accountType  默认是 2 企业
     * @param operatorId   默认是 companyId
     * @return  true : 是  false ： 否
     */
    Boolean findIsIdCardCreateCompany(Integer accountType, String operatorId);
}
