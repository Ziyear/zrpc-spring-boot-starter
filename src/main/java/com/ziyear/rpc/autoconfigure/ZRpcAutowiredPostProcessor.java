package com.ziyear.rpc.autoconfigure;

import com.ziyear.rpc.annotation.ZRpcAutowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述 : 服务发现，扫描处理 ZRpcAutowired 注解
 *
 * @author Ziyear 2021-06-10 20:44
 */
@Slf4j
public class ZRpcAutowiredPostProcessor implements ApplicationContextAware, BeanFactoryPostProcessor, BeanClassLoaderAware {


    private ClassLoader classloader;
    private ApplicationContext context;

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classloader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        postProcessBeanFactory(beanFactory, (BeanDefinitionRegistry) beanFactory);
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory, BeanDefinitionRegistry registry) {
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (BeanDefinition.ROLE_APPLICATION != beanDefinition.getRole()) {
                continue;
            }
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName == null) {
                continue;
            }
            Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, classloader);
            ReflectionUtils.doWithFields(beanClass, this::doField);
        }

        for (String beanName : beanDefinitionMap.keySet()) {
            if (context.containsBean(beanName)) {
                throw new RuntimeException("注解 ZRpcAutowired 下的[" + beanName + "]已经在容器中注册，请修改！");
            }

            registry.registerBeanDefinition(beanName, beanDefinitionMap.get(beanName));
            log.info("bean [{}] is registered.", beanName);
        }
    }

    private void doField(Field field) {
        ZRpcAutowired annotation = field.getAnnotation(ZRpcAutowired.class);
        if (annotation == null) {
            return;
        }
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ZRpcAutowiredProxyBean.class);
        builder.addPropertyValue("rpcInterface", field.getType());
        builder.addPropertyValue("annotation", annotation);
        beanDefinitionMap.put(field.getName(), builder.getBeanDefinition());
    }
}
