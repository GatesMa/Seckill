package com.gatesma.dao;

import com.gatesma.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit Spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SuccessKilledDao successKilledDao;
    
    @Test
    public void insertSuccessKilled() {
        long id = 1001L;
        long phone = 15667462250L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("-------------");
        System.out.println("insertCount: " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() {
        
        long id = 1001L;
        long phone = 15667462250L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println("============================================================");
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }
}