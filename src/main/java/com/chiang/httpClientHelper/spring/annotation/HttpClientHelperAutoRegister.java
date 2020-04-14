package com.chiang.httpClientHelper.spring.annotation;


import com.chiang.httpClientHelper.spring.selector.HttpClientHelperImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HttpClientHelperImportSelector.class)
public @interface HttpClientHelperAutoRegister {
}
