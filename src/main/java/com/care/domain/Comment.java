package com.care.domain;

import com.care.domain.base.BaseModel;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nujian on 16/2/26.
 */
@Entity
@SQLDelete(sql = "update comment_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Comment extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "comment_pictures_",
            joinColumns = {@JoinColumn(name = "comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "picture_id")})
    private List<Picture> pictures;

    @Column(columnDefinition = "text")
    private String content;

    private BigDecimal star;

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

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getStar() {
        return star;
    }

    public void setStar(BigDecimal star) {
        this.star = star;
    }

    public static List<Comment> getUserComment(Integer userId,Integer page,Integer count){
        String query = "from Comment c where c.order.nurse.id =:user_id or c.user.id=:user_id order by c.id desc";
        return entityManager().createQuery(query,Comment.class).setParameter("user_id",userId)
                .setFirstResult((page - 1)* count).setMaxResults(count).getResultList();
    }
}
