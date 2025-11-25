package com.xtm.contract.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.thread.executor.CustomThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@Slf4j
public class ThreadPoolConfig {

    // 当前机器的CPU核心数
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    @Bean(name = TmsContractConstant.COMMON_THREAD_POOL_NAME)
    public Executor asyncExecutor() {
        log.info("线程池配置完成,当前机器的CPU核心数:{}", AVAILABLE_PROCESSORS);
        ThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        //线程池核心线程，正常情况下开启的线程数. CPU核数 + 1
        executor.setCorePoolSize(AVAILABLE_PROCESSORS + 1);
        //最大线程数
        executor.setMaxPoolSize(AVAILABLE_PROCESSORS * 2 + 1);
        //线程队列容量。当核心线程数都被占用，多余的任务会存到此处
        executor.setQueueCapacity(200);
        //设置线程活跃时间（线程池中空闲线程等待工作的超时时间）
        executor.setKeepAliveSeconds(60);
        //线程名字前缀
        executor.setThreadNamePrefix(TmsContractConstant.COMMON_THREAD_POOL_NAME + StrUtil.DASHED);
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
    
}