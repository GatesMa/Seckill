package com.gatesma.exception;

/**
 * Copyright (C), 2019
 * FileName: RepeatKillException
 * Author:   Marlon
 * Date:     2019/12/3 13:33
 * Description: 重复秒杀异常(运行期异常)
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
