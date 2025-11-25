package com.xtm.contract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.xtm"})
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.xtm"},exclude = {RedisReactiveAutoConfiguration.class})
public class ContractApplication {
	public static void main(String[] args) {
		SpringApplication.run(ContractApplication.class, args);
        log.info("====> tms-contract 启动完成 <====");
	}

}
