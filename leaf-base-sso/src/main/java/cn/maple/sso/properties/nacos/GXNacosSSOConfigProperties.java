package cn.maple.sso.properties.nacos;

import cn.maple.sso.properties.GXSSOConfigProperties;
import cn.maple.sso.properties.GXSSOProperties;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
@SuppressWarnings("all")
@EqualsAndHashCode(callSuper = true)
@ConditionalOnClass(name = {"com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties"})
@NacosPropertySource(dataId = "sso.yml", groupId = "${spring.cloud.nacos.config.group:${nacos.config.group:}}", properties = @NacosProperties(serverAddr = "${spring.cloud.nacos.config.server-addr:${nacos.config.server-addr:}}", namespace = "${spring.cloud.nacos.config.namespace:${nacos.config.namespace:}}", username = "${spring.cloud.nacos.username:${nacos.config.username:}}", password = "${spring.cloud.nacos.password:${nacos.config.password:}}"))
@ConfigurationProperties(prefix = "sso")
public class GXNacosSSOConfigProperties extends GXSSOConfigProperties {
    @NestedConfigurationProperty
    private GXSSOProperties config = new GXSSOProperties();

    public GXNacosSSOConfigProperties() {
        log.info("SSO的配置使用的是NACOS配置");
    }

    @Override
    public GXSSOProperties getConfig() {
        return config;
    }
}
