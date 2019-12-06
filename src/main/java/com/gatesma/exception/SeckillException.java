package com.gatesma.exception;

/**
 * Copyright (C), 2019
 * FileName: SeckillException
 * Author:   Marlon
 * Date:     2019/12/3 13:36
 * Description: 秒杀相关业务异常
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
