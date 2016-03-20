package com.care.domain;

import com.care.domain.base.BaseModel;
import flexjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nujian on 16/2/24.
 */
@Entity
@SQLDelete(sql = "update price_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Price extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    private BigDecimal price;

    @Column(name = "memo",length = 150)
    private String memo;

    @JSON(include = false)
    @OneToMany(mappedBy = "price")
    private List<PriceChangeLog> changeLogs;

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<PriceChangeLog> getChangeLogs() {
        return changeLogs;
    }

    public void setChangeLogs(List<PriceChangeLog> changeLogs) {
        this.changeLogs = changeLogs;
    }


    public static Price getLastPrice(){
        String query = "from Price p order by p.id desc";
        List<Price> prices = entityManager().createQuery(query,Price.class).setMaxResults(1).getResultList();
        return CollectionUtils.isEmpty(prices)?null:prices.get(0);
    }
}
