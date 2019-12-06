package com.gatesma.dto;

/**
 * Copyright (C), 2019
 * FileName: SeckillResult
 * Author:   Marlon
 * Email: gatesma@foxmail.com
 * Date:     2019/12/4 17:20
 * Description:
 */
//所有的ajax请求的返回类型：封装json结果
public class SeckillResult<T> {

    private boolean success;


    private T data;

    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
