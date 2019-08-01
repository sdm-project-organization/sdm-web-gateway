package com.mo.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {

        HttpHeaders headers = request.getHeaders();

        // HTTP 헤더에 상관관계 ID 추가
        headers.add(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        // HTTP 헤더에 인증토큰을 추가
        headers.add(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
        // HTTP 헤더에 Authorization 추가
        headers.add(UserContext.AUTHORIZATION, UserContextHolder.getContext().getAuthorization());

        return execution.execute(request, body);
    }
}