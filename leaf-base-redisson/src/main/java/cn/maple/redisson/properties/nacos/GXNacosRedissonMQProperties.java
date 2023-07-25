package cn.maple.redisson.properties.nacos;

import cn.maple.redisson.properties.GXRedissonConnectProperties;
import cn.maple.redisson.properties.GXRedissonMQProperties;
import cn.maple.redisson.properties.GXRedissonProperties;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosPropertySource(dataId = "redisson-mq.yml",
        groupId = "${spring.cloud.nacos.config.group:${nacos.config.group:}}",
        properties = @NacosProperties(
                serverAddr = "${spring.cloud.nacos.config.server-addr:${nacos.config.server-addr:}}",
                namespace = "${spring.cloud.nacos.config.namespace:${nacos.config.namespace:}}",
                username = "${spring.cloud.nacos.username:${nacos.config.username:}}",
                password = "${spring.cloud.nacos.password:${nacos.config.password:}}"))
@ConfigurationProperties(prefix = "redisson")
public class GXNacosRedissonMQProperties extends GXRedissonMQProperties {
    private Map<String, GXRedissonConnectProperties> config = new LinkedHashMap<>();

    public GXNacosRedissonMQProperties() {
        log.info("Redisson数据源的配置使用的是NACOS配置");
    }

    @Override
    public Map<String, GXRedissonConnectProperties> getConfig() {
        return config;
    }
}
