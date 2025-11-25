package com.xtm.contract.listener;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private final NacosAutoServiceRegistration nacosAutoServiceRegistration;
    private final ApplicationContext context;

    /**
     * 注销服务后关闭应用前等待的时间（毫秒）
     */
    private int waitTime=2000;

    @PostConstruct
    public void shutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("-----------------注销nacos---------");
            try {
                nacosAutoServiceRegistration.stop();
            }catch (Exception e){
                log.error("注销nacos异常："+e);
            }
            //TODO 停止rocketmq消费
//            consumer.shutdown();
        }));
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        //开启异步线程，先从nacos注销，等待waitTime毫秒后，关闭容器
        log.info("等待一段时间");
        try{
            Thread.sleep(waitTime);
        }catch (Exception e){
            log.error("睡眠异常："+e);
        }

        new Thread(()->{
            log.info("开始关闭容器");
            SpringApplication.exit(context);
            ((ConfigurableApplicationContext)context).close();
        }).start();

        log.info("关闭服务完成");
    }
}