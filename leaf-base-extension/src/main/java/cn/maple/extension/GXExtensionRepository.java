package cn.maple.extension;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtensionRepository 扩展仓库 保存扩展信息
 *
 * @author britton
 */
@Getter
@Component
public class GXExtensionRepository {
    /**
     * 扩展点缓存对象
     * -- GETTER --
     * 获取扩展点缓存对象
     */
    private final Map<GXExtensionCoordinate, GXExtensionPoint> extensionRepo = new ConcurrentHashMap<>();
}
