package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.embeddables.Location;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nujian on 16/2/18.
 */
@Entity
@Table(name = "api_invoke_log_")
@NamedQueries({
        @NamedQuery(name = "getLastApiInvokeLogByUserId", query = "from ApiInvokeLog ail where ail.user.id=:user_id order by ail.createTime desc"),
})
public class ApiInvokeLog extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Index(name = "user_id")
    private User user;

    @Column(name = "action_")
    private String action;

    private long costTime;

    private String userAgent;

    private Location location;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static ApiInvokeLog getLastApiInvokeLogByUserId(Integer userId){
        List<ApiInvokeLog> logs = null;
        try{
            logs = entityManager().createNamedQuery("getLastApiInvokeLogByUserId",ApiInvokeLog.class).setParameter("user_id",userId).setMaxResults(1).getResultList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return CollectionUtils.isNotEmpty(logs)? logs.get(0):null;
    }
}
