package com.mo.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN = "tmx-auth-token";
    public static final String USER_ID = "tmx-user-id";
    public static final String ORG_ID = "tmx-org-id";
    public static final String AUTHORIZATION = "Authorization";

    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ROUTE_FILTER_TYPE = "route";

    public String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // validation Header
        if (ctx.getRequest().getHeader(CORRELATION_ID) != null) {
            return ctx.getRequest().getHeader(CORRELATION_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(CORRELATION_ID);
        }
    }

    public void setCorrelationId(String correlationId){
        RequestContext ctx = RequestContext.getCurrentContext();

        // 주울 서버의 필터를 지나는 동안 추가되는 별도의 HTTP 헤더 맵을 관리
        // ZuulRequestHeader 맵에 보관된 데이터는 주울 서버가 대상 서비스를 호출할 때 합쳐짐
        ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
    }

    public final String getOrgId() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(ORG_ID) !=null) {
            return ctx.getRequest().getHeader(ORG_ID);
        } else {
            return  ctx.getZuulRequestHeaders().get(ORG_ID);
        }
    }

    public void setOrgId(String orgId) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(ORG_ID,  orgId);
    }

    public final String getUserId(){
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getHeader(USER_ID) !=null) {
            return ctx.getRequest().getHeader(USER_ID);
        }
        else{
            return  ctx.getZuulRequestHeaders().get(USER_ID);
        }
    }

    public void setUserId(String userId){
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(USER_ID,  userId);
    }

    public final String getAuthToken(){
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }

    public String getServiceId(){
        RequestContext ctx = RequestContext.getCurrentContext();

        //We might not have a service id if we are using a static, non-eureka route.
        if (ctx.get("serviceId")==null) return "";
        return ctx.get("serviceId").toString();
    }

    public String getAuthorization() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // validation Header
        if (ctx.getRequest().getHeader(AUTHORIZATION) != null) {
            return ctx.getRequest().getHeader(AUTHORIZATION);
        } else{
            return  ctx.getZuulRequestHeaders().get(AUTHORIZATION);
        }
    }

    // HTTP 헤더에서 tmx-correlation-id 설정
    public void setAuthorization(String authorization){
        RequestContext ctx = RequestContext.getCurrentContext();

        // 주울 서버의 필터를 지나는 동안 추가되는 별도의 HTTP 헤더 맵을 관리
        // ZuulRequestHeader 맵에 보관된 데이터는 주울 서버가 대상 서비스를 호출할 때 합쳐짐
        ctx.addZuulRequestHeader(AUTHORIZATION, authorization);
    }

}
