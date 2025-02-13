package cn.maple.core.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * 在使用数据源切换时
 * 1. 如果调用的是项目内的功能， 需要在XXXRepository上添加@GXDataSource("other")
 * 2. 如果需要调用MyBatis Plus封装的功能 ，需要在XXXService上添加@GXDataSource("other")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GXDataSource {
    String value() default "";
}
