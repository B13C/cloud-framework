package cn.maple.core.framework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXParam {
    /**
     * 参数名字
     */
    String[] paramNames();

    /**
     * 是否必须
     */
    boolean require() default true;
}
