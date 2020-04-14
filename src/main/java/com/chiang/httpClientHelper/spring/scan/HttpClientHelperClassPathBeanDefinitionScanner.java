package com.chiang.httpClientHelper.spring.scan;


import com.chiang.httpClientHelper.spring.factory.ClientFactoryBean;
import lombok.Data;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

@Data
public class HttpClientHelperClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public HttpClientHelperClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    private Class<? extends Annotation> annotationClass;

    private Class<?> markerInterface;

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitions = super.doScan(basePackages);
        for (BeanDefinitionHolder definition : definitions) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) definition.getBeanDefinition();
            String beanClassName = beanDefinition.getBeanClassName();
            beanDefinition.setBeanClass(ClientFactoryBean.class);
            beanDefinition.setBeanClassName(ClientFactoryBean.class.getName());
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
        }
        return definitions;
    }

    public void registerFilters() {
        boolean acceptAllInterfaces = true;

        // if specified, use the given annotation and / or marker interface
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
            acceptAllInterfaces = false;
        }

        // override AssignableTypeFilter to ignore matches on the actual marker interface
        if (this.markerInterface != null) {
            addIncludeFilter(new AssignableTypeFilter(this.markerInterface) {
                @Override
                protected boolean matchClassName(String className) {
                    return false;
                }
            });
            acceptAllInterfaces = false;
        }

        if (acceptAllInterfaces) {
            // default include filter that accepts all classes
            addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        }

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
