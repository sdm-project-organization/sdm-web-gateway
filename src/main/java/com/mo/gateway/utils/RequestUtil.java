package com.mo.gateway.utils;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

public class RequestUtil {

    // TODO static env property mapping
    @Value("${mo.rest.pathVariable}")
    public static String PATH_VARIABLE = "{}";

    public static Map<String, Object> getNextMap(Map<String, Object> parentMap, String key) throws RuntimeException {
        // TODO CastingException handleing
        Map<String, Object> result = (Map) parentMap.get(key);
        if(result == null) {
            result = (Map) parentMap.get(PATH_VARIABLE);
        }
        // TODO specific
        if(result == null) {
            throw new RuntimeException();
        }
        return result;
    }

    public static List<String> getRoleList(Map<String, Object> parentMap, String key) throws RuntimeException {
        // TODO CastingException handleing
        List<String> result = (List) parentMap.get(key);

        // TODO specific
        if(result == null)
            throw new RuntimeException();
        return result;
    }

}
