package com.care.domain;

import com.care.domain.base.BaseModel;
import flexjson.JSON;
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
@SQLDelete(sql = "update user_wallet_changelog_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserWalletChangeLog extends BaseModel {

    @JSON(include = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private UserWallet wallet;

    @Column(name = "memo",length = 200)
    private String memo;

    @JSON(include = false)
    private BigDecimal balanceBefore;

    @JSON(include = false)
    private BigDecimal balanceAfter;

    @JSON(include = true)
    @Transient
    public BigDecimal getChangeCost(){
        return balanceAfter.subtract(balanceBefore).setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public UserWallet getWallet() {
        return wallet;
    }

    public void setWallet(UserWallet wallet) {
        this.wallet = wallet;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public static List<UserWalletChangeLog> getWalletChangelogsByUser(UserWallet wallet,Integer page,Integer count){
        String query = "from UserWalletChangeLog uwcl where uwcl.wallet.id=:wallet_id order by uwcl.id desc";
        return entityManager().createQuery(query,UserWalletChangeLog.class).setParameter("wallet_id",wallet.getId())
                .setFirstResult((page - 1) * count).setMaxResults(count).getResultList();
    }
}
