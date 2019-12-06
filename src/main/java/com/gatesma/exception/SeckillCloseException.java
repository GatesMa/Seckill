package com.gatesma.exception;

/**
 * Copyright (C), 2019
 * FileName: SeckillCloseException
 * Author:   Marlon
 * Date:     2019/12/3 13:35
 * Description: 秒杀关闭异常
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
