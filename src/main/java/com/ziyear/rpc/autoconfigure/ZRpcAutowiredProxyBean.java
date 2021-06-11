package com.ziyear.rpc.autoconfigure;

import com.ziyear.rpc.annotation.ZRpcAutowired;
import com.ziyear.zrpc.core.ZRpcApplication;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 功能描述 : ZRpcAutowired 注解代理类
 *
 * @author Ziyear 2021-6-10 20:40
 */
public class ZRpcAutowiredProxyBean implements FactoryBean {


    private Class<?> rpcInterface;

    private ZRpcAutowired annotation;

    @Autowired
    private ZRpcApplication zRpcApplication;

    public Class<?> getRpcInterface() {
        return rpcInterface;
    }

    public void setRpcInterface(Class<?> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    public ZRpcAutowired getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ZRpcAutowired annotation) {
        this.annotation = annotation;
    }

    /**
     * 功能描述 : 用描述文件,生成真正对象的时候,会调用这个方法,调用的时候生成代理对象
     *
     * @return {@link Object}
     * @author Ziyear 2021-6-10 20:41
     */
    @Override
    public Object getObject() throws Exception {
        String serviceName = annotation.name();
        if ("".equals(serviceName)) {
            serviceName = rpcInterface.getName();
        }
        return zRpcApplication.getService(serviceName, rpcInterface);
    }

    /**
     * 功能描述 : 假装我的类型还是 原来的接口类型,不是代理对象,这样 自动注入的时候,类型才能匹配上
     *
     * @return {@link Class}
     * @author Ziyear 2021-6-10 20:42
     */
    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
}
