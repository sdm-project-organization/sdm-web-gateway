package com.mo.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class GuardDiscoveryClient {

    public static final String GATEWAY_NAME = "gateway";
    public static final String GUARD_NAME = "guard";

    @Autowired
    private DiscoveryClient discoveryClient;

    public Map<String, List<String>> getRoles(String organizationId) {
        RestTemplate restTemplate = new RestTemplate();

        // 조직 서비스의 모든 인스턴스 목록 얻기 (ServiceInstance)
        List<ServiceInstance> instances = discoveryClient.getInstances(GUARD_NAME);
        if (instances.size()==0) return null;

        // 호출할 서비스 엔드포인트 조회
        String serviceUri = String.format("%s/roles/organizationname/%s",
                instances.get(0).getUri().toString(), organizationId);

        // 서비스를 호출하는데 표준 스프링 RestTemplate 클래스 사용
        ResponseEntity<Map> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, Map.class, organizationId);

        return restExchange.getBody();
    }
}