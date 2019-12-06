package com.gatesma.service.impl;

import com.gatesma.dao.SeckillDao;
import com.gatesma.dao.SuccessKilledDao;
import com.gatesma.dao.cache.RedisDao;
import com.gatesma.dto.Exposer;
import com.gatesma.dto.SeckillExecution;
import com.gatesma.entity.Seckill;
import com.gatesma.entity.SuccessKilled;
import com.gatesma.enums.SeckillStateEnum;
import com.gatesma.exception.RepeatKillException;
import com.gatesma.exception.SeckillCloseException;
import com.gatesma.exception.SeckillException;
import com.gatesma.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2019
 * FileName: SeckillServiceImpl
 * Author:   Marlon
 * Date:     2019/12/3 16:44
 * Description: 秒杀Service的实现
 */
//@Component 所有组件 @Serivce @DAO @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Resource @Inject这些junit的一些规范注解，Spring用AutoWired
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    //md5盐值字符串，用于混淆md5
    private final String slat = "sisdjancucns9afqhefuqf#*(Q#&jwfnjqnfq@*(1ßxxs";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.quertAll(0, 100);
    }

    @Override
    public Seckill getById(long secKillId) {
        return seckillDao.queryById(secKillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        // 优化：缓存优化; 建立在超时的基础上
        //1。访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3。放入Redis
                redisDao.putSeckill(seckill);
            }
        }


        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点
     * 1：开发团队一致约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短，不要穿插其他的网络操作
     * 3：不是所有的方法都需要事务，如只有一条修改操作等
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("Seckill Data Rewrite");
        }
        //执行秒杀逻辑: 减库存 + 记录购买行为
        Date nowTime = new Date();

        try {

            //减库存成功,记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //唯一：seckillId, userPhone
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("Seckill Repeated");
            } else {
                //减储存， 热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有更新到记录，秒杀结束
                    throw new SeckillCloseException("Seckill Is Closed");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译期异常，转化为运行期异常
            //==================重点=================
            //转化为运行期异常后，Spring会对所有的运行期异常做RollBack（回滚），所以在这里就不用声明事务了
            //==================重点=================
            throw new SeckillException("Seckill inner Error: " + e.getMessage());
        }
    }


    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行完了之后，result被赋值
        try {
            seckillDao.killByProcedure(map);
            //获取result
            Integer result = MapUtils.getInteger(map, "result", -2);
//            logger.info("result:" + result);
//            System.out.println(result);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
            } else {
                return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
        }
    }
}