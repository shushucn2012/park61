package com.park61.moduel.pay.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 余额明细详情
 */
public class BillDetail implements Serializable {
    private BigDecimal availableOperateAmount;//运转金额
    private BigDecimal availableAfterAmount;//可用余额
    private String createTime;
    private long id;
    private Long orderId;//订单id
    private String remark;
    private int type;
    private Integer rechargeType;// 充值类型(0:微信;1:支付宝)

    public BigDecimal getAvailableAfterAmount() {
        return availableAfterAmount;
    }

    public void setAvailableAfterAmount(BigDecimal availableAfterAmount) {
        this.availableAfterAmount = availableAfterAmount;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
