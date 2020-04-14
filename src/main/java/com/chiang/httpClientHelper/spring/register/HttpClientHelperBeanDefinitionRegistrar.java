package com.chiang.httpClientHelper.spring.register;

import com.chiang.httpClientHelper.annotation.Client;
import com.chiang.httpClientHelper.spring.scan.HttpClientHelperClassPathBeanDefinitionScanner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.env.Environment;


import static org.springframework.context.annotation.AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR;

@Slf4j
@Data
public class HttpClientHelperBeanDefinitionRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    private final String SCAN_PACKAGES_PROPERTY = "spring.cloud.chiang.httpClientHelper.scanBasePackages";

    private BeanNameGenerator resolveBeanNameGenerator(BeanDefinitionRegistry registry) {
        BeanNameGenerator beanNameGenerator = null;

        if (registry instanceof SingletonBeanRegistry) {
            SingletonBeanRegistry singletonBeanRegistry = SingletonBeanRegistry.class.cast(registry);
            beanNameGenerator = (BeanNameGenerator) singletonBeanRegistry.getSingleton(CONFIGURATION_BEAN_NAME_GENERATOR);
        }

        if (beanNameGenerator == null) {

            if (log.isInfoEnabled()) {

                log.info("BeanNameGenerator bean can't be found in BeanFactory with name ["
                        + CONFIGURATION_BEAN_NAME_GENERATOR + "]");
                log.info("BeanNameGenerator will be a instance of " +
                        AnnotationBeanNameGenerator.class.getName() +
                        " , it maybe a potential problem on bean name generation.");
            }

            beanNameGenerator = new AnnotationBeanNameGenerator();

        }

        return beanNameGenerator;

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        HttpClientHelperClassPathBeanDefinitionScanner scanner = new HttpClientHelperClassPathBeanDefinitionScanner(registry);
        String packages = environment.getProperty(SCAN_PACKAGES_PROPERTY);
        Class<?> springbootApplication = deduceMainApplicationClass();
        if (springbootApplication != null && springbootApplication.isAnnotationPresent(SpringBootApplication.class) && StringUtils.isEmpty(packages)) {
            String basePackages = springbootApplication.getPackage().getName();
            packages = basePackages;
        }
        String[] packagesToScan = StringUtils.isEmpty(packages) ? new String[]{"*.*"} : packages.split(",");
        BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
        scanner.setBeanNameGenerator(beanNameGenerator);
        scanner.setAnnotationClass(Client.class);
        scanner.registerFilters();
        scanner.scan(packagesToScan);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }
}
