package com.chiang.httpClientHelper.spring.configuration;

import com.chiang.httpClientHelper.annotation.Client;
import com.chiang.httpClientHelper.spring.register.HttpClientHelperBeanDefinitionRegistrar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.omg.PortableInterceptor.RequestInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({OkHttpClient.class, Request.class, Response.class, RequestInfo.class, Client.class})
public class HttpClientHelperAutoConfiguration {
    private static final String HTTP_CLIENT_HELPER_BEAN_NAME = "com.chiang.httpClientHelper.spring.register.HttpClientHelperBeanDefinitionRegistrar";


    @Bean(HTTP_CLIENT_HELPER_BEAN_NAME)
    public HttpClientHelperBeanDefinitionRegistrar httpClientHelperBeanDefinitionRegistrar() {
        return new HttpClientHelperBeanDefinitionRegistrar();
    }
}
