package com.chiang.httpClientHelper.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@HttpClientHelperAutoRegister
public @interface EnableHttpClientHelper {
}
