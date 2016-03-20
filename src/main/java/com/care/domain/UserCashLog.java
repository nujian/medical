package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.CashStatus;
import flexjson.JSON;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by nujian on 16/2/29.
 */
@Entity
@SQLDelete(sql = "update user_cash_log_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserCashLog extends BaseModel {

    @JSON(include = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    @Index(name = "status")
    private CashStatus status;

    private BigDecimal cost;

    @Column(length = 200)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Column(name = "care_num",length = 20)
    private String cardNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_info_id")
    private UserWalletBankInfo bankInfo;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CashStatus getStatus() {
        return status;
    }

    public void setStatus(CashStatus status) {
        this.status = status;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public UserWalletBankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(UserWalletBankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}
