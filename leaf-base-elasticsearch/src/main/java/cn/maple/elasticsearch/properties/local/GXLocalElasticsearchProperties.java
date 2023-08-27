package cn.maple.elasticsearch.properties.local;

import cn.maple.core.framework.factory.GXYamlPropertySourceFactory;
import cn.maple.elasticsearch.properties.GXElasticsearchProperties;
import cn.maple.elasticsearch.properties.GXElasticsearchSourceProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 每个应用自定义的环境配置文件
 */
@Data
@Slf4j
@Primary
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnMissingClass({"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@PropertySource(value = {"classpath:/${spring.profiles.active}/elasticsearch.yml"}, factory = GXYamlPropertySourceFactory.class, ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "elasticsearch")
public class GXLocalElasticsearchProperties extends GXElasticsearchSourceProperties {
    private Map<String, GXElasticsearchProperties> datasource = new LinkedHashMap<>();

    public GXLocalElasticsearchProperties() {
        log.info("Elasticsearch数据源的配置使用的是本地配置");
    }

    @Override
    public Map<String, GXElasticsearchProperties> getDatasource() {
        return datasource;
    }
}
