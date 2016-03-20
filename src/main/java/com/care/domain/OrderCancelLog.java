package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.UserType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nujian on 16/3/8.
 */
@Entity
@SQLDelete(sql = "update order_cancel_log_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class OrderCancelLog extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UserType userType;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public static List<OrderCancelLog> findLogByOrder(Order order){
        String query = "from OrderCancelLog ol where ol.order.id=:order_id order by ol.id desc";
        return entityManager().createQuery(query,OrderCancelLog.class).setParameter("order_id",order.getId()).getResultList();
    }
}
