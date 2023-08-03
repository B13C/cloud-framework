package cn.maple.core.datasource.annotation;

import cn.maple.core.datasource.service.GXMybatisListenerService;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXMyBatisListener {
    Class<? extends GXMybatisListenerService> listenerClazz();
}
