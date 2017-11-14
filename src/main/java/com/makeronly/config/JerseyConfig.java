package com.makeronly.config;

import com.makeronly.dfs.resource.FileResource;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.stereotype.Component;

/**
 * @author Walter Wong
 */
@Component
public class JerseyConfig extends ResourceConfig {
    /**
     * 扫描com.makeronly包，使其识别JAX-RS注解
     */
    public JerseyConfig() {
        register(RequestContextFilter.class);
        // 注册文件上传
        register(MultiPartFeature.class);

        register(FileResource.class);
    }
}
