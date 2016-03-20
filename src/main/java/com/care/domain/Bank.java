package com.care.domain;

import com.care.domain.base.BaseModel;
import flexjson.JSON;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by nujian on 16/3/6.
 */
@Entity
@SQLDelete(sql = "update bank_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Bank extends BaseModel {

    @JSON(include = false)
    @Column(name = "bank_code",length = 7)
    @Index(name = "code")
    private String bankCode;

    @NotNull
    @Column(name = "bank_name",length = 100)
    private String bankName;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public static Bank find(Integer id){
        return entityManager().find(Bank.class,id);
    }

    public static List<Bank> findBanks(){
        String query = "from Bank b order b.id desc";
        return entityManager().createQuery(query,Bank.class).getResultList();
    }
}
