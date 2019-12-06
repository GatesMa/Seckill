package com.gatesma.service;

import com.gatesma.dto.Exposer;
import com.gatesma.dto.SeckillExecution;
import com.gatesma.entity.Seckill;
import com.gatesma.exception.RepeatKillException;
import com.gatesma.exception.SeckillCloseException;
import com.gatesma.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在"使用者"的角度设计接口
 * 方法定义粒度，参数，返回类型（return 类型友好/异常）
 */
public interface SeckillService {

    /**
     * 查询所有秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param secKillId
     * @return
     */
    Seckill getById(long secKillId);

    /**
     * 秒杀开启时，输出秒杀接口的地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 执行秒杀操作  --  存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
