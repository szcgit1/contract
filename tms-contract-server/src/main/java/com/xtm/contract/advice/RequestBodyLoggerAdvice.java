package com.xtm.contract.advice;

import com.xtm.contract.controller.thirdparty.ThirdPartyFrameworkContractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


/**
 * 请求体日志记录器
 * <br>
 * 未开启全局拦截, 当前只对注解配置的Controller生效
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 09:46
 */
@Slf4j
@ControllerAdvice(assignableTypes = {
        ThirdPartyFrameworkContractController.class,
})
public class RequestBodyLoggerAdvice extends RequestBodyAdviceAdapter {
    
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String methodName = Optional.ofNullable(parameter.getMethod())
                .map(Method::getName)
                .orElse("未知方法");
        // 获取请求体中的body,并转换为String
        byte[] body = StreamUtils.copyToByteArray(inputMessage.getBody());
        if (0 < body.length) {
            String bodyString = new String(body, StandardCharsets.UTF_8);
            log.info("\n====> 请求报文前置日志记录 - 调用方法: {}.{}() - 原始报文:\n{}\n<====", parameter.getContainingClass().getSimpleName(), methodName, bodyString);
        } else {
            log.info("\n====> 请求报文前置日志记录 - 调用方法: {}.{}() - 原始报文为空 <====", parameter.getContainingClass().getSimpleName(), methodName);
        }
        // 将字节数组重新包装成一个新的HttpInputMessage，以便后续的Spring反序列化流程可以正常读取
        return new HttpInputMessage() {
            @Override
            public InputStream getBody() {
                return new ByteArrayInputStream(body);
            }
            
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }
        };
    }
    
}
