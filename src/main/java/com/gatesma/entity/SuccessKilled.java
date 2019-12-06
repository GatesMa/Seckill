package com.gatesma.entity;

import java.util.Date;

/**
 * Copyright (C), 2019
 * FileName: SuccessKilled
 * Author:   Marlon
 * Date:     2019/12/1 21:43
 * Description:
 */
public class SuccessKilled {

    private long seckillId;

    private long userrPhone;

    private short state;

    private Date createTime;


    // 多对一
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserrPhone() {
        return userrPhone;
    }

    public void setUserrPhone(long userrPhone) {
        this.userrPhone = userrPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userrPhone=" + userrPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                ", seckill=" + seckill +
                '}';
    }
}
