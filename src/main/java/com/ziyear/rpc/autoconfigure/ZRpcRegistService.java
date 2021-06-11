package com.ziyear.rpc.autoconfigure;

import com.ziyear.rpc.annotation.ZRpcService;
import com.ziyear.zrpc.core.ZRpcApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * 服务注册
 */
public class ZRpcRegistService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ZRpcApplication zRpcApplication;

    /**
     * 扫描自定义的注解,并注册到RPC容器中
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //拿到spring容器
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        //拿到所有打了注解 RpcService 的bean
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ZRpcService.class);

        boolean isStart = false;

        for (String serviceName : beansWithAnnotation.keySet()) {

            Object serviceObject = beansWithAnnotation.get(serviceName);
            Class<?> serviceImpClass = serviceObject.getClass();
            //拿到注解
            ZRpcService rpcService = serviceImpClass.getAnnotation(ZRpcService.class);

            String registerBeanName = rpcService.name();
            //如果在注解中没指定名称,就用 接口的名称,
            if ("".equals(registerBeanName)) {
                Class<?>[] interfaces = serviceImpClass.getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    registerBeanName = interfaces[0].getName();
                }
            }

            if (registerBeanName == null || "".equals(registerBeanName)) {
                System.err.println(serviceObject.getClass().getName() + " ---> 注册失败");
                continue;
            }
            try {
                zRpcApplication.register(registerBeanName, serviceObject);
                isStart = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (isStart) {
            System.out.println("RPC 服务提供者 开启 ---- > 端口 " + zRpcApplication.getPort());
        }
    }
}
