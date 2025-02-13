package cn.maple.core.datasource.aspect;

import cn.maple.core.datasource.annotation.GXDataSource;
import cn.maple.core.datasource.config.GXDynamicContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 线程数据源切换处理类
 *
 * 在使用数据源切换时
 * 1. 如果调用的是项目内的功能， 需要在XXXRepository上添加@GXDataSource("other")
 * 2. 如果需要调用MyBatis Plus封装的功能 ，需要在XXXService上添加@GXDataSource("other")
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GXDataSourceAspect {
    @Pointcut("@annotation(cn.maple.core.datasource.annotation.GXDataSource) " +
            "|| @within(cn.maple.core.datasource.annotation.GXDataSource) " +
            "|| target(cn.maple.core.datasource.service.GXMyBatisBaseService) " +
            "|| target(cn.maple.core.datasource.repository.GXMyBatisRepository)")
    public void dataSourcePointCut() {
        // 这是是切点标记
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();
        GXDataSource targetDataSourceAnnotation = targetClass.getAnnotation(GXDataSource.class);
        GXDataSource methodDataSourceAnnotation = method.getAnnotation(GXDataSource.class);
        if (targetDataSourceAnnotation != null || methodDataSourceAnnotation != null) {
            String value;
            value = Objects.requireNonNullElse(methodDataSourceAnnotation, targetDataSourceAnnotation).value();
            GXDynamicContextHolder.push(value);
            log.debug("{}线程设置的数据源是{}", Thread.currentThread().getName(), value);
        }
        try {
            return point.proceed();
        } finally {
            GXDynamicContextHolder.poll();
            log.debug("{}线程清除数据源", Thread.currentThread().getName());
        }
    }
}