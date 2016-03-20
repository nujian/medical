package com.care.domain;

import com.care.domain.base.BaseModel;
import flexjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nujian on 16/3/1.
 */
@Entity
@SQLDelete(sql = "update user_wallet_bank_info_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserWalletBankInfo extends BaseModel {

    @JSON(include = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private UserWallet wallet;

    @Column(name = "card_num",length = 50)
    @Index(name = "care_num")
    private String cardNum;

    @Column(length = 200)
    private String bankName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    public UserWallet getWallet() {
        return wallet;
    }

    public void setWallet(UserWallet wallet) {
        this.wallet = wallet;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public static UserWalletBankInfo find(Integer id){
        return entityManager().find(UserWalletBankInfo.class,id);
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public static UserWalletBankInfo findBankInfoByCardNum(String cardNum){
        String query = "from UserWalletBankInfo ubi where ubi.cardNum=:card_num";
        List<UserWalletBankInfo> bankInfos = entityManager().createQuery(query,UserWalletBankInfo.class).setParameter("card_num",cardNum).getResultList();
        return CollectionUtils.isNotEmpty(bankInfos)?bankInfos.get(0):null;
    }
}
