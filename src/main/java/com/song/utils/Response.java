package com.song.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Song on 2018/2/27.
 */
@Data
public class Response<T> implements Serializable {
    public static final int SUCCESS = 1;
    public static final int FAILED = 0;

    private int code;
    private String msg;
    private T body;

    private Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public static <T> Response success() {
        return new Response(SUCCESS, "success");
    }

    public static <T> Response success(T t) {
        return new Response(SUCCESS, "success", t);
    }

    public static Response failed() {
        return new Response(FAILED, "failed");
    }

    public static <T> Response failed(T t) {
        return new Response(FAILED, "failed", t);
    }
}
