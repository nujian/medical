package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.PayChannel;
import com.care.domain.enums.TradingStatus;
import flexjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by nujian on 16/2/23.
 */
@Configurable
@Entity
@SQLDelete(sql = "update trading_record_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class TradingRecord extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id")
    private User payer;


    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    private PayChannel channel;

    /**
     * 第三方订单号
     */
    @Column(name = "third_parts_trading_num",length = 60)
    @Index(name = "third_parts_trading_num")
    private String thirdPartsTradingNum;

    @Enumerated(EnumType.STRING)
    private TradingStatus status;


    @Column(name = "memo",length = 100)
    private String memo;

    @JSON(include = false)
    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date payTime;

    /**
     * 取消时间
     */
    @JSON(include = false)
    @Basic(fetch = FetchType.LAZY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancelTime;


    @Column(name = "pay_id",length = 20)
    private String payIp;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public PayChannel getChannel() {
        return channel;
    }

    public void setChannel(PayChannel channel) {
        this.channel = channel;
    }

    public String getThirdPartsTradingNum() {
        return thirdPartsTradingNum;
    }

    public void setThirdPartsTradingNum(String thirdPartsTradingNum) {
        this.thirdPartsTradingNum = thirdPartsTradingNum;
    }

    public TradingStatus getStatus() {
        return status;
    }

    public void setStatus(TradingStatus status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getPayIp() {
        return payIp;
    }

    public void setPayIp(String payIp) {
        this.payIp = payIp;
    }

    public static TradingRecord find(Integer recordId){
        return entityManager().find(TradingRecord.class,recordId);
    }

    public static TradingRecord getTradingRecordByOrder(Order order){
        String query = "from TradingRecord tr where tr.order.id=:order_id order";
        List<TradingRecord> records = entityManager().createQuery(query,TradingRecord.class).setParameter("order_id",order.getId()).setMaxResults(1).getResultList();
        return CollectionUtils.isNotEmpty(records)?records.get(0):null;
    }
}
