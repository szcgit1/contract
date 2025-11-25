package com.xtm.contract.feign.fallback;

import com.alibaba.fastjson.JSON;
import com.xtm.contract.feign.DocumentFeginClient;
import com.xtm.contract.model.query.contractOther.DocumentInfoQryIn;
import com.xtm.contract.model.query.contractOther.PaymentSchedule;
import com.xtm.contract.model.query.contractOther.TransportChargeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 16:21
 * @desc
 */
@Component
@Slf4j
public class DocumentFallBack implements DocumentFeginClient {
    @Override
    public TransportChargeDetail selectTransportChargeDetail(DocumentInfoQryIn documentInfoQryIn) {
        log.error("调用 base-tms 服务查询单据费用科目详情接口失败 参数：{}", JSON.toJSONString(documentInfoQryIn));
        return null;
    }

    @Override
    public List<PaymentSchedule> selectPaymentSchedule(DocumentInfoQryIn documentInfoQryIn) {
        log.error("调用 base-tms 服务查询单据付款进度接口失败 参数：{}", JSON.toJSONString(documentInfoQryIn));
        return null;
    }
}
