package com.care.domain;

import com.care.domain.base.BaseModel;
import flexjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nujian on 16/2/19.
 */
@Configurable
@Entity
@SQLDelete(sql = "update user_wallet_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserWallet extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JSON(include = false)
    @Column(name = "password",length = 100)
    private String password;

    private BigDecimal balance;

    @JSON(include = false)
    @OneToMany(mappedBy = "wallet")
    private List<UserWalletChangeLog> walletChangeLogs;

    @JSON(include = false)
    @OneToMany(mappedBy = "wallet")
    private List<UserWalletBankInfo> bankInfos;

    @Transient
    private boolean settingPassword;

    public boolean isSettingPassword() {
        return StringUtils.isNotBlank(password);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<UserWalletChangeLog> getWalletChangeLogs() {
        return walletChangeLogs;
    }

    public void setWalletChangeLogs(List<UserWalletChangeLog> walletChangeLogs) {
        this.walletChangeLogs = walletChangeLogs;
    }

    public List<UserWalletBankInfo> getBankInfos() {
        return bankInfos;
    }

    public void setBankInfos(List<UserWalletBankInfo> bankInfos) {
        this.bankInfos = bankInfos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static UserWallet find(Integer walletId){
        return entityManager().find(UserWallet.class,walletId);
    }
}
