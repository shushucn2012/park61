package com.park61.moduel.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 余额明细列表数据
 */
public class BillInfo implements Serializable {
    private BigDecimal availableAfterAmount;//可用余额
    private BigDecimal availableOperateAmount;//运转金额
    private String createTime;//日期
    private long id;
    private int type;
    private String remark;//内容

    public BigDecimal getAvailableAfterAmount() {
        return availableAfterAmount;
    }

    public void setAvailableAfterAmount(BigDecimal availableAfterAmount) {
        this.availableAfterAmount = availableAfterAmount;
    }

    public BigDecimal getAvailableOperateAmount() {
        return availableOperateAmount;
    }

    public void setAvailableOperateAmount(BigDecimal availableOperateAmount) {
        this.availableOperateAmount = availableOperateAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
