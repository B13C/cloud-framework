package cn.maple.extension;

import cn.hutool.http.HttpStatus;
import cn.maple.extension.exception.GXExtensionException;
import cn.maple.extension.register.GXAbstractComponentExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * ExtensionExecutor 扩展执行器
 *
 * @author britton
 */
@Component
@Slf4j
public class GXExtensionExecutor extends GXAbstractComponentExecutor {
    @Resource
    private GXExtensionRepository extensionRepository;

    @Override
    protected <C> C locateComponent(Class<C> targetClz, GXBizScenario bizScenario) {
        C extension = locateExtension(targetClz, bizScenario);
        log.debug("[Located Extension]: {}", extension.getClass().getSimpleName());
        return extension;
    }

    /**
     * if the bizScenarioUniqueIdentity is "gaple.xx.supermarket"
     * <p>
     * the search path is as below:
     * 1、first try to get extension by "gaple.xx.supermarket", if get, return it.
     * 2、loop try to get extension by "gaple.xx", if get, return it.
     * 3、loop try to get extension by "gaple", if get, return it.
     * 4、if not found, try the default extension
     *
     * @param targetClz 目标类型
     */
    protected <E> E locateExtension(Class<E> targetClz, GXBizScenario bizScenario) {
        checkNull(bizScenario);

        E extension;

        log.debug("BizScenario in locateExtension is : {}", bizScenario.getUniqueIdentity());

        // 1、 first try with full namespace
        extension = firstTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // 2、 second try with default scenario
        extension = secondTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        // 3、 third try with default use case + default scenario
        extension = defaultUseCaseTry(targetClz, bizScenario);
        if (extension != null) {
            return extension;
        }

        String errMessage = "Can not find extension with ExtensionPoint: " + targetClz + " BizScenario:" + bizScenario.getUniqueIdentity();
        throw new GXExtensionException(errMessage, HttpStatus.HTTP_NOT_FOUND);
    }

    /**
     * first try with full namespace
     * <p>
     * example:  biz1.useCase1.scenario1
     */
    private <E> E firstTry(Class<E> targetClz, GXBizScenario bizScenario) {
        log.debug("First trying with {}", bizScenario.getUniqueIdentity());
        return locate(targetClz.getName(), bizScenario.getUniqueIdentity());
    }

    /**
     * second try with default scenario
     * <p>
     * example:  biz1.useCase1.#defaultScenario#
     */
    private <E> E secondTry(Class<E> targetClz, GXBizScenario bizScenario) {
        log.debug("Second trying with {}", bizScenario.getIdentityWithDefaultScenario());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultScenario());
    }

    /**
     * third try with default use case + default scenario
     * <p>
     * example:  biz1.#defaultUseCase#.#defaultScenario#
     */
    private <E> E defaultUseCaseTry(Class<E> targetClz, GXBizScenario bizScenario) {
        log.debug("Third trying with {}", bizScenario.getIdentityWithDefaultUseCase());
        return locate(targetClz.getName(), bizScenario.getIdentityWithDefaultUseCase());
    }

    /**
     * 从扩展库中寻找扩展对象
     *
     * @param name           扩展名字
     * @param uniqueIdentity 扩展唯一标识
     * @param <E>            类型
     * @return E
     */
    @SuppressWarnings("all")
    private <E> E locate(String name, String uniqueIdentity) {
        GXExtensionCoordinate coordinate = new GXExtensionCoordinate(name, uniqueIdentity);
        return (E) extensionRepository.getExtensionRepo().get(coordinate);
    }

    /**
     * NULL检查
     *
     * @param bizScenario 业务场景对象
     */
    private void checkNull(GXBizScenario bizScenario) {
        if (bizScenario == null) {
            throw new IllegalArgumentException("BizScenario can not be null for extension");
        }
    }
}
