package com.chiang.httpClientHelper.spring.factory;

import com.chiang.httpClientHelper.assistant.InstanceAssistant;
import org.springframework.beans.factory.FactoryBean;

public class ClientFactoryBean<T> implements FactoryBean<T> {
    private Class<T> interfaceClass;

    public ClientFactoryBean(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T getObject() throws Exception {
        try {
            return InstanceAssistant.newInstance(interfaceClass);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public Class<?> getObjectType() {
        return interfaceClass;
    }
}
