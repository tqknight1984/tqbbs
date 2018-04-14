package com.poly.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiyuan_fengyu on 2017/1/6.
 */
public class ResponseUtil {

    public static Map<String, Object> success(String message) {
        return success(message, null);
    }

    public static Map<String, Object> success(String message, Object data) {
        return create(true, message, data);
    }

    public static Map<String, Object> fail(String message) {
        return create(false, message, null);
    }

    public static Map<String, Object> create(boolean success, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("message", message);
        map.put("data", data);
        return map;
    }

}
