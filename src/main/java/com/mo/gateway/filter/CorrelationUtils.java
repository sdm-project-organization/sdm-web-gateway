package com.mo.gateway.filter;

public class CorrelationUtils {

    public static String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }
}
