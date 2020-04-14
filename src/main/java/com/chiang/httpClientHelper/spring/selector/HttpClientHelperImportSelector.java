package com.chiang.httpClientHelper.spring.selector;

import com.chiang.httpClientHelper.spring.annotation.HttpClientHelperAutoRegister;
import org.springframework.cloud.commons.util.SpringFactoryImportSelector;

public class HttpClientHelperImportSelector extends SpringFactoryImportSelector<HttpClientHelperAutoRegister> {
    @Override
    protected boolean isEnabled() {
        return getEnvironment().getProperty("spring.cloud.chiang.httpClientHelper.enabled",
                Boolean.class, Boolean.TRUE);
    }

}
