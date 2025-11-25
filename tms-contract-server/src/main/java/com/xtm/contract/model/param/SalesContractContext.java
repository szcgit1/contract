package com.xtm.contract.model.param;

import com.xtm.common.model.Result;
import com.xtm.company.model.vo.CompanyVo;
import com.xtm.contract.model.domain.SalesContract;
import com.xtm.contract.model.vo.frameAgreement.FrameAgreementHistoryFieldVo;
import com.xtm.thirdparty.data.model.vo.ProductLineVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Data
public class SalesContractContext {

    private boolean isUpdate;

    private SalesContract tmsContract;

    //记录基础信息内部匹配日志
    private StringBuilder baseInfoMatchErrorMsgBuilder = new StringBuilder();

    //存储发运公司信息 key 税号 value 公司信息
    private Map<String, CompanyVo> shippingCompanyMap = new HashMap<>();

    //客户
    private CompanyVo customer;

    //主表产品线
    private ProductLineVo mainProductLine;

    //销售组织
    private CompanyVo salesOrg;

    //匹配框架合同结果
    private Result<String> matchAgreementResult;

    //匹配托盘客户结果
    private Result<String> matchTrayCustomerResult;

    //取消前记录
    private FrameAgreementHistoryFieldVo cancelBeforeAgreement;

    //取消后记录
    private FrameAgreementHistoryFieldVo cancelAfterAgreement;

    //关联前记录
    private FrameAgreementHistoryFieldVo relateBeforeAgreement;

    //关联后记录
    private FrameAgreementHistoryFieldVo relateAfterAgreement;


}
