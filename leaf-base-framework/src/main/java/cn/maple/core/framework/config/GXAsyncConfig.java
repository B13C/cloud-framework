package cn.maple.core.framework.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Log4j2
@Configuration
public class GXAsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        threadPoolTaskExecutor.setCorePoolSize(1);
        // 设置最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(3);
        // 设置队列大小
        threadPoolTaskExecutor.setQueueCapacity(20000);
        // 设置线程活跃时间(秒)
        threadPoolTaskExecutor.setKeepAliveSeconds(120);
        // 设置线程名前缀
        threadPoolTaskExecutor.setThreadNamePrefix("maple-framework-async-thread-");
        // 设置线程分组名称
        threadPoolTaskExecutor.setThreadGroupName("maple-framework-async-thread-group");
        // 设置拒绝的执行处理策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 所有任务结束后关闭线程池
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            List<String> errors = CollUtil.newArrayList();
            errors.add("--------------Maple Leaf FrameWork异步调用，异常捕获--------------");
            errors.add(CharSequenceUtil.format("Exception message - {}", throwable.getMessage()));
            errors.add(CharSequenceUtil.format("Method name - {}", method.getName()));
            for (Object param : obj) {
                errors.add(CharSequenceUtil.format("Parameter value - {}", param));
            }
            errors.add("--------------Maple Leaf FrameWork异步调用，异常捕获--------------");
            String errorMsg = CollUtil.join(errors, System.lineSeparator());
            log.error(errorMsg, throwable);
        };
    }
}
