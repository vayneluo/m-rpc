package me.lattice.mrpc.client.boot;

import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.client.annotation.MRpcReference;
import me.lattice.mrpc.client.bean.MRpcProxyBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: lattice
 * @date: 2022/12/9 4:45 PM
 */
@Slf4j
@Component
public class MRpcBeanFactoryProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {

    /** 容器上下文 **/
    private ApplicationContext applicationContext;

    /** 当前类加载器 **/
    private ClassLoader classLoader;

    /** 存储代理对象的Bean描述信息 **/
    private final Map<String, BeanDefinition> proxyBeanDefinitionMap = new LinkedHashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    /**
     * 扫描所有的Bean，找到所有的MRpcReference注解，构造代理对象BeanDefinition，交给Spring容器管理
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).forEach(beanName -> {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (Objects.nonNull(beanClassName)) {
                Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, classLoader);
                ReflectionUtils.doWithFields(beanClass, this::generateProxyBeanDefinition);
            }
        });
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        proxyBeanDefinitionMap.forEach((beanName, beanDefinition) -> {
            // 重名Bean处理
            if (this.applicationContext.containsBean(beanName)) {
                throw new IllegalArgumentException("The bean name " + beanName + " is already used!");
            }
            registry.registerBeanDefinition(beanName, beanDefinition);
        });
    }

    /**
     * 生成代理对象的Bean描述信息
     * @param field
     */
    private void generateProxyBeanDefinition(Field field) {
        // 判断是否有 @MRpcReference 注解
        MRpcReference mRpcReference = AnnotationUtils.getAnnotation(field, MRpcReference.class);
        if (Objects.nonNull(mRpcReference)) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MRpcProxyBean.class);
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("registryType", mRpcReference.registryType());
            builder.addPropertyValue("registryAddress", mRpcReference.registryAddress());
            builder.addPropertyValue("version", mRpcReference.version());
            builder.addPropertyValue("readTimeout", mRpcReference.readTimeout());
            builder.addPropertyValue("connectTimeout", mRpcReference.connectTimeout());
            // 将代理对象的Bean描述信息存储到 proxyBeanDefinitionMap 中
            proxyBeanDefinitionMap.put(field.getName(), builder.getBeanDefinition());
        }
    }
}

