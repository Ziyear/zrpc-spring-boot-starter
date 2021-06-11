package com.ziyear.rpc.autoconfigure;

import com.ziyear.rpc.properties.ZRpcProperties;
import com.ziyear.zrpc.core.ZRpcApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ZRpcApplication.class)
@EnableConfigurationProperties(ZRpcProperties.class)
public class ZRpcAutoConfigure {

    @Autowired
    ZRpcProperties zRpcProperties;

    @Bean
    @ConditionalOnMissingBean(ZRpcApplication.class)
    public ZRpcApplication initZRpcApplication() {
        ZRpcApplication zRpcApplication = new ZRpcApplication(zRpcProperties.getZookeepeer());
        zRpcApplication.setPort(zRpcProperties.getPort());
        zRpcApplication.setIp(zRpcProperties.getIp());
        return zRpcApplication;
    }

    /**
     * 扫描所有打过注解的类,把他注册进rpc服务
     */
    @ConditionalOnBean(ZRpcApplication.class)
    @Bean
    public ZRpcRegistService registerAllService() {
        return new ZRpcRegistService();
    }


    @Bean
    public static ZRpcAutowiredPostProcessor zRpcAutowiredPostProcessor() {
        return new ZRpcAutowiredPostProcessor();
    }
}
