package com.jiaozx.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName JsonResult
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/7/12 18:30
 * @Version 1.0
 */

@Getter
@Setter
@NoArgsConstructor
public class JSONResult {

    private int code;
    private String msg;
    private Object data;

    public JSONResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JSONResult success() {
        return new JSONResult(200, null, null);
    }

    public static JSONResult success(String msg) {
        return new JSONResult(200, msg, null);
    }

    public static JSONResult success(Object data) {
        return new JSONResult(200, null, data);
    }

    public static JSONResult success(String msg, Object data) {
        return new JSONResult(200, msg, data);
    }

    public static JSONResult error(String msg) {
        return new JSONResult(500, msg, null);
    }

}
