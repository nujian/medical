package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.PriceChangeType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by nujian on 16/2/24.
 */
@Entity
@SQLDelete(sql = "update price_change_log_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class PriceChangeLog extends BaseModel {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Enumerated(EnumType.STRING)
    private PriceChangeType type;

    @Column(name = "chang_before",length = 40)
    private String changBefore;

    @Column(name = "changAfter",length = 40)
    private String changAfter;

    @Column(name = "memo",length = 100)
    private String memo;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getChangBefore() {
        return changBefore;
    }

    public void setChangBefore(String changBefore) {
        this.changBefore = changBefore;
    }

    public String getChangAfter() {
        return changAfter;
    }

    public void setChangAfter(String changAfter) {
        this.changAfter = changAfter;
    }

    public PriceChangeType getType() {
        return type;
    }

    public void setType(PriceChangeType type) {
        this.type = type;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
