package com.geoxus.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.util.GXResultUtils;
import com.geoxus.core.common.util.GXSpringContextUtils;
import com.geoxus.plugins.impl.GXLoginSsoPlugin;
import com.geoxus.sso.cache.GXSsoCache;
import com.geoxus.sso.config.GXSsoConfig;
import com.geoxus.sso.enums.GXTokenOrigin;
import com.geoxus.sso.security.token.GXSsoToken;
import com.geoxus.sso.util.GXSsoHelperUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/")
public class LoginController {
    @PostMapping("login")
    public GXResultUtils<Dict> login(HttpServletRequest request, HttpServletResponse response) {
        GXSsoToken ssoToken = GXSsoToken.create();
        GXSsoConfig ssoConfig = new GXSsoConfig();
        ssoConfig.setPluginList(Collections.singletonList(new GXLoginSsoPlugin()))
                .setCache(GXSpringContextUtils.getBean(GXSsoCache.class));
        GXSsoHelperUtil.setSsoConfig(ssoConfig).setSsoToken(ssoToken).getSsoToken()
                .setUserId(1)
                .setIssuer("admin")
                .setUserAgent("我的agent")
                .setOrigin(GXTokenOrigin.HTML5)
                .setTenantId(12899)
                .setTime(System.currentTimeMillis());
        String token = ssoToken.getToken();
        GXSsoHelperUtil.setCookie(request, response, ssoToken);
        return GXResultUtils.ok(Dict.create().set("token", token));
    }
}
