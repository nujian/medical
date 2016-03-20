package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.SmsType;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by nujian on 16/2/23.
 */
@Entity
@SQLDelete(sql = "update sms_verify_log_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class SmsVerifyLog extends BaseModel {


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "birth_date")
    private Date verifiedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @NotNull
    private String mobile;

    @NotNull
    private String verifyCode;

    @NotNull
    private boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    private SmsType type;

    public Date getVerifiedTime() {
        return verifiedTime;
    }

    public void setVerifiedTime(Date verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public SmsType getType() {
        return type;
    }

    public void setType(SmsType type) {
        this.type = type;
    }


    public static SmsVerifyLog getLastUnVerifyLogByMobileAndType(String mobile,SmsType type){
        String query = "from SmsVerifyLog log where log.mobile=:mobile and log.type=:type order by id desc";
        try{
            return entityManager().createQuery(query,SmsVerifyLog.class).setParameter("mobile",mobile).setParameter("type",type).setMaxResults(1).getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
