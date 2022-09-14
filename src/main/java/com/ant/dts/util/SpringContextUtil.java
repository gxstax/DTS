package com.ant.dts.util;

import com.ant.dts.resources.ResourceStore;
import com.ant.dts.resources.partition.TopicPartitionRule;
import io.micrometer.core.lang.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Spring 容器工具
 * </p>
 *
 * @author Ant
 * @since 2022/1/1 3:31 下午
 */
@Component
public class SpringContextUtil<T> implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 通过name获取 Bean (类型不安全)
     *
     * @param name
     * @return If the bean does not exist，
     *         an exception{@link NoSuchBeanDefinitionException} will be thrown
     */
    public static Object getBean(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取 Bean (类型安全)
     *
     * @param clazz
     * @return 如果 Bean 不存在，返回 null
     */
    public static Object getBean(Class clazz) {
        final ObjectProvider beanProvider = applicationContext.getBeanProvider(clazz);
        return beanProvider.getIfAvailable();
    }

    /**
     * <p>
     * 通过 class 获取 Beans
     * </p>
     *
     * @param
     * @return Objects
     */
    public static Map<String, Object> getBeansOfType(Class clazz) {
        final Map<String, Object> beansOfType = applicationContext.getBeansOfType(clazz);
        return beansOfType;
    }

    /**
     * <p>
     * 获取主题分区规则
     * </p>
     *
     * @param
     * @return
     */
    public static List<TopicPartitionRule> getTopicPartitionRule() {
        final ResourceStore resourceStore = applicationContext.getBean(ResourceStore.class);
        if (ObjectUtils.isEmpty(resourceStore)) {
            return new ArrayList<>();
        }
        return resourceStore.getPartitionRule();
    }

}
