package com.gatesma.service;

import com.gatesma.dto.Exposer;
import com.gatesma.dto.SeckillExecution;
import com.gatesma.entity.Seckill;
import com.gatesma.exception.RepeatKillException;
import com.gatesma.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final String slat = "sisdjancucns9afqhefuqf#*(Q#&jwfnjqnfq@*(1ßxxs";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() {
        long id = 1000;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("Exposer: " + exposer);
        //INFO  c.gatesma.service.SeckillServiceTest -
        // Exposer: Exposer{exposed=true, md5='0bb5be3c4bb2befc7cfb828f21598ece', seckillId=1001, now=0, start=0, end=0}
    }

    @Test
    public void executeSeckill() {
        long id = 1001;
        long phone = 15667462254L;
        String md5 = "0bb5be3c4bb2befc7cfb828f21598ece";

        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
            logger.info("result={}", seckillExecution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        } catch (SeckillCloseException e) {
            logger.error(e.getMessage());
        }
    }

    //测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("Exposer: " + exposer);
            long phone = 15667462250L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, phone, md5);
                logger.info("result={}", seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }

    @Test
    public void testExecuteSeckillProcedure() {
        long seckillId = 1001;
        long phone = 15109161848L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
//        logger.info(exposer.getMd5());
//        logger.info(getMD5(seckillId));
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
        String md5 = null;
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}