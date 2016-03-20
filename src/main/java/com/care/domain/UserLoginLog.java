package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.embeddables.Location;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nujian on 16/2/22.
 */
@Entity
@SQLDelete(sql = "update user_login_log_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserLoginLog extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "user_login_token" ,length = 100)
    @Index(name = "user_login_token")
    private String userLoginToken;

    @Column(length = 160)
    private String userAgent;

    private boolean isActive = true;

    @Embedded
    private Location location;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUserLoginToken() {
        return userLoginToken;
    }

    public void setUserLoginToken(String userLoginToken) {
        this.userLoginToken = userLoginToken;
    }

    public static UserLoginLog getActiveLogByToken(String token) {
        String query = "from UserLoginLog log  where log.isActive = true and log.userLoginToken=:token order by id desc ";
        List<UserLoginLog> logs = entityManager().createQuery(query,UserLoginLog.class).setParameter("token",token).setMaxResults(1).getResultList();
        return CollectionUtils.isEmpty(logs)?null:logs.get(0);
    }

    public static UserLoginLog getLastActiveLogByUserId(Integer userId){
        String query = "from UserLoginLog log where log.isActive = true and log.user.id=:user_id order by id desc";
        List<UserLoginLog> logs = entityManager().createQuery(query,UserLoginLog.class).setParameter("user_id",userId).setMaxResults(1).getResultList();
        return CollectionUtils.isEmpty(logs)?null:logs.get(0);
    }
}
