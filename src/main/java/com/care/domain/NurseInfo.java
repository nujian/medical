package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.utils.DateUtils;
import flexjson.JSON;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nujian on 16/2/22.
 */
@Entity
@SQLDelete(sql = "update nurse_info_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class NurseInfo extends BaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JSON(include = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Index(name = "birth_date")
    private Date workDate;

    /**
     * 工龄
     */
    @JSON(include = true)
    @Transient
    private Integer seniority;


    @Column(name = "title",length = 100)
    private String title;

    @Transient
    @JSON(include = true)
    public Integer getSeniority() {
        if (workDate != null) {
            return DateUtils.diffYear(new Date(), workDate);
        }
        return 1;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_photo_id")
    private Picture workPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Picture certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Picture getWorkPhoto() {
        return workPhoto;
    }

    public void setWorkPhoto(Picture workPhoto) {
        this.workPhoto = workPhoto;
    }

    public Picture getCertification() {
        return certification;
    }

    public void setCertification(Picture certification) {
        this.certification = certification;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static NurseInfo findNurseByUser(User user){
        String query = "from NurseInfo n where n.user.id=:user_id";
        try{
            return entityManager().createQuery(query,NurseInfo.class).setParameter("user_id",user.getId()).getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
