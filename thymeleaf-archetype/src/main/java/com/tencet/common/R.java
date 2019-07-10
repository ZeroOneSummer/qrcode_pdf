package com.tencet.common;

import lombok.Data;

/**
 * Created by pijiang on 2019/7/2.
 */
@Data
public class R {

    private Integer code;
    private String message;
    private Object data;

    private R(Integer code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /*
    * ok
    */
    public static R OK(Integer code, String message, Object data){
        return new R(code, message, data);
    }

    public static R OK(String message, Object data){
        return R.OK(200, message, data);
    }

    public static R OK(String message){
        return R.OK(message, null);
    }

    public static R OK(Object data){
        return R.OK("success", data);
    }

    public static R OK(){
        return R.OK("success", null);
    }

    /*
     * error
     */
    public static R ERROR(Integer code, String message){
        return new R(code, message, null);
    }

    public static R ERROR(Integer code){
        return new R(code, "error", null);
    }
}