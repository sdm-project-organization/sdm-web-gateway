package com.mo.gateway;


import com.mo.gateway.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;


@SpringBootApplication
@EnableZuulProxy
public class GatewayApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}

