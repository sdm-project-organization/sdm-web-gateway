package com.mo.gateway.security;

import com.mo.gateway.client.GuardDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class ResourceRoleConfiguration {

    @Autowired
    GuardDiscoveryClient guardDiscoveryClient;

    @Bean
    public Map<String, Object> getRoleMap() {
        return guardDiscoveryClient.getRoles("hello");
    }
}
