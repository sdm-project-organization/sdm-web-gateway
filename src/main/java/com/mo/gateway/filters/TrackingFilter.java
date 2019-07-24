package com.mo.gateway.filters;

import com.mo.gateway.config.ServiceConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 사전필터
 * HTTP 헤더에 tmx-correlation-id가 없다면 TrackingFilter가 상관관계 ID를 생성하고 설정
 * 상관관계 ID가 이미 포함되어 있다면 주울은 상관관계 ID에 대해 아무일도 하지 않음
 * */
@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    // 모든 필터에서 공통으로 사용되는 유틸
    @Autowired
    FilterUtils filterUtils;

    @Autowired
    ServiceConfig serviceConfig;

    // 사전 / 경로 / 사후 필터를 지정하는데 사용
    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE; // 사전필터
    }

    // 다른 필터 유형으로 요청을 보내야 하는 순서를 나타내는 정수 값
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    // 필터의 활성화 여부를 나타내는 불값 반환
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    // 필터의 활성화 여부를 나타내는 불값 반환
    private boolean isCorrelationIdPresent(){
      if (filterUtils.getCorrelationId() !=null){
          return true;
      }
      return false;
    }

    // 상관관계 ID 생성
    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public Object run() {
        if (isCorrelationIdPresent()) {
           logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        } else {
            // 상관관계 ID 설정
            filterUtils.setCorrelationId(generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        RequestContext ctx = RequestContext.getCurrentContext();
        logger.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());
        return null;
    }

    /*private String getOrganizationId() {
        String result = "";
        if(filterUtils.getAuthToken() != null) {
            // HTTP `Authorization` 헤더에서 토큰을 파싱
            String authToken = filterUtils.getAuthToken().replace("Bearer ", "");

            try {
                // 토큰서명에 사용된 서명키를 전달하며, Jwts 클래스를 사용해 토큰 파싱
                Claims claims = Jwts.parser()
                        .setSigningKey(serviceConfig.getJwtSigningKey().getBytes("UTF-8"))
                        .parseClaimsJws(authToken).getBody();
                result = (String) claims.get("organizationId");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }*/

}