package com.xtm.contract.feign;

import com.xtm.contract.feign.fallback.DocumentFallBack;
import com.xtm.contract.model.query.contractOther.DocumentInfoQryIn;
import com.xtm.contract.model.query.contractOther.PaymentSchedule;
import com.xtm.contract.model.query.contractOther.TransportChargeDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
* @Author: fengyj
* @Description: 订单服务feign接口
* @Date: Create in 14:26 2022/12/17
*/
@FeignClient(value = "base-tms",fallback = DocumentFallBack.class)
public interface DocumentFeginClient {
    /**
     * 查询
     * @param documentInfoQryIn
     */
    @PostMapping(value = "/apiPlat/tms/charge/detailByChargeId")
    TransportChargeDetail selectTransportChargeDetail(@RequestBody DocumentInfoQryIn documentInfoQryIn);

    /**
     * 查询单据付款方式
     * @param documentInfoQryIn
     * @return
     */
    @PostMapping(value = "/apiPlat/tms/paymentSchedule/listByDocument")
    List<PaymentSchedule> selectPaymentSchedule(@RequestBody DocumentInfoQryIn documentInfoQryIn);
}
