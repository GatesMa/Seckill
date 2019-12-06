package com.gatesma.dao.cache;

import com.gatesma.dao.SeckillDao;
import com.gatesma.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019
 * FileName: RedisDaoTest
 * Author:   Marlon
 * Email: gatesma@foxmail.com
 * Date:     2019/12/5 21:26
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit Spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    
    private long id = 1001;

    @Autowired
    private SeckillDao seckillDao;
    
    
    @Autowired
    private RedisDao redisDao;
    
    @Test
    public void getSeckill() {
    
        Seckill seckill = redisDao.getSeckill(id);
        if(seckill == null) {
            seckill = seckillDao.queryById(id);
            if(seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }
        
    }

    @Test
    public void putSeckill() {
    }
}