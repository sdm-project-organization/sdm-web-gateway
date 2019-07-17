//package com.thoughtmechanix.zuulsvr.filters;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import com.thoughtmechanix.zuulsvr.config.ServiceConfig;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TrackingFilter extends ZuulFilter{
//    private static final int      FILTER_ORDER =  1;
//    private static final boolean  SHOULD_FILTER=true;
//    private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
//
//    @Autowired
//    FilterUtils filterUtils;
//
//    @Autowired
//    ServiceConfig serviceConfig;
//
//    @Override
//    public String filterType() {
//        return FilterUtils.PRE_FILTER_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return FILTER_ORDER;
//    }
//
//    public boolean shouldFilter() {
//        return SHOULD_FILTER;
//    }
//
//    private boolean isCorrelationIdPresent(){
//      if (filterUtils.getCorrelationId() !=null){
//          return true;
//      }
//
//      return false;
//    }
//
//    private String generateCorrelationId(){
//        return java.util.UUID.randomUUID().toString();
//    }
//
//    public Object run() {
//
//        if (isCorrelationIdPresent()) {
//           logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
//        }
//        else{
//            filterUtils.setCorrelationId(generateCorrelationId());
//            logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
//        }
//
//        RequestContext ctx = RequestContext.getCurrentContext();
//        logger.debug("Processing incoming request for {}.",  ctx.getRequest().getRequestURI());
//        return null;
//    }
//
//    private String getOrganizationId() {
//        String result = "";
//        if(filterUtils.getAuthToken() != null) {
//            // HTTP `Authorization` 헤더에서 토큰을 파싱
//            String authToken = filterUtils.getAuthToken().replace("Bearer ", "");
//
//            try {
//                // 토큰서명에 사용된 서명키를 전달하며, Jwts 클래스를 사용해 토큰 파싱
//                Claims claims = Jwts.parser()
//                        .setSigningKey(serviceConfig.getJwtSigningKey().getBytes("UTF-8"))
//                        .parseClaimsJws(authToken).getBody();
//                result = (String) claims.get("organizationId");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//}