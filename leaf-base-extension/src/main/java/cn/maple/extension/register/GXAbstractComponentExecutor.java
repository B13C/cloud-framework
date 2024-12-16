package cn.maple.extension.register;

import cn.maple.extension.GXBizScenario;
import cn.maple.extension.GXExtensionCoordinate;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 抽象组件执行器
 * 用于执行扩展功能
 *
 * @author britton
 */
public abstract class GXAbstractComponentExecutor {
    /**
     * Execute extension with Response
     *
     * @param targetClz   目标类型
     * @param bizScenario 业务场景
     * @param exeFunction 执行函数
     * @param <R>         Response Type
     * @param <T>         Parameter Type
     * @return R
     */
    public <R, T> R execute(Class<T> targetClz, GXBizScenario bizScenario, Function<T, R> exeFunction) {
        T component = locateComponent(targetClz, bizScenario);
        return exeFunction.apply(component);
    }

    /**
     * Execute extension with Response
     *
     * @param extensionCoordinate 扩展点
     * @param exeFunction         执行函数
     * @param <T>                 函数参数类型
     */
    public <R, T> R execute(GXExtensionCoordinate extensionCoordinate, Function<T, R> exeFunction) {
        return execute(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    /**
     * Execute extension without Response
     *
     * @param targetClz   目标类型
     * @param context     业务场景
     * @param exeFunction 执行函数
     * @param <T>         T
     */
    public <T> void executeVoid(Class<T> targetClz, GXBizScenario context, Consumer<T> exeFunction) {
        T component = locateComponent(targetClz, context);
        exeFunction.accept(component);
    }

    /**
     * Execute extension without Response
     *
     * @param extensionCoordinate 扩展点
     * @param exeFunction         执行函数
     * @param <T>                 函数参数类型
     */
    public <T> void executeVoid(GXExtensionCoordinate extensionCoordinate, Consumer<T> exeFunction) {
        executeVoid(extensionCoordinate.getExtensionPointClass(), extensionCoordinate.getBizScenario(), exeFunction);
    }

    protected abstract <C> C locateComponent(Class<C> targetClz, GXBizScenario context);
}
