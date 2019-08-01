package com.mo.gateway.filter;

import com.mo.gateway.config.ServiceConfig;
import com.mo.gateway.security.ResourceRoleConfiguration;
import com.mo.gateway.security.ResourceServerConfiguration;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 사전필터
 *
 * HTTP 헤더에 tmx-correlation-id가 없다면 `TrackingFilter` 가 상관관계 ID를 생성하고 설정
 * 상관관계 ID가 이미 포함되어 있다면 주울은 상관관계 ID에 대해 아무일도 하지 않음
 * */
@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    ServiceConfig serviceConfig;

    @Autowired
    ResourceRoleConfiguration resourceRoleConfiguration;

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

    private List<String> getAuthorities() {
        List<String> authorities = new ArrayList<>();
        if(filterUtils.getAuthorization() != null) {
            // HTTP `Authorization` 헤더에서 토큰을 파싱
            String authToken = filterUtils.getAuthorization().replace("Bearer ", "");

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(serviceConfig.getJwtSigningKey().getBytes("UTF-8"))
                        .parseClaimsJws(authToken).getBody();
                authorities = (List<String>) claims.get("authorities");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return authorities;
    }

    private boolean access(List<String> myRoles, String reqPath) {
        Map<String, List<String>> guardRoles = resourceRoleConfiguration.getRoles();
        boolean result = myRoles.stream().anyMatch(myRole -> {
            List<String> resources = guardRoles.get(myRole);
            return resources.indexOf(reqPath) > 0;
        });
        return result;
    }

    /**
     * 필터에 접근할 수 있는 2가지 경우
     *
     * 1. 인증 요청일때
     * 2. JWT를 가지고 있을 때
     * */
    @Override
    public Object run() throws RuntimeException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String organization = "";
        String reqPath = ctx.getRequest().getRequestURI();

        if(reqPath.equals(ResourceServerConfiguration.PATH_LOGIN)) {
            // [CASE1] LOGIN LOGIC
            filterUtils.setAuthorization(ctx.getRequest().getHeader(FilterUtils.AUTHORIZATION));
        } else {
            // [CASE2] JWT LOGIC
            if(!access(getAuthorities(), reqPath)) {
                throw new RuntimeException();
            }
        }


        // * 상관관계 ID 설정
        if (isCorrelationIdPresent()) {
           logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
        } else {
            filterUtils.setCorrelationId(CorrelationUtils.generateCorrelationId());
            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
        }

        logger.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());
        return null;
    }

}