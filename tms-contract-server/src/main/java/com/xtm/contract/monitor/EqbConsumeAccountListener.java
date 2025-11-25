package com.xtm.contract.monitor;

import cn.hutool.core.util.StrUtil;
import com.xtm.contract.model.query.listener.EqbConsumeAccountReq;
import com.xtm.contract.utils.EqbHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/31 12:39
 * @desc
 */
@Component
@Slf4j
public class EqbConsumeAccountListener implements SmartApplicationListener {

    @Autowired
    @Lazy
    private EqbHelper eqbHelper;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        EqbConsumeAccountReq accountReq = (EqbConsumeAccountReq) event;
        if(StrUtil.isBlank(accountReq.getContractId())){
            log.info("同步流水的合同ID不能为空！！");
            return;
        }
        log.info("Login in Transfer");
        try{
        if ("deductionEqbFlow".equals(accountReq.getSource())) {
            log.info("Transfer start !");
           boolean flag = eqbHelper.consumeAccountPay(accountReq);
           if(flag == true){
               log.info("Transfer success !");
           } else {
               log.info("Transfer failure!!!");
           }
        }
        }catch (Exception e){
            log.error("消费e签宝流水扣除失败"+e.getMessage(),e);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }


    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == EqbConsumeAccountReq.class;
    }
}
