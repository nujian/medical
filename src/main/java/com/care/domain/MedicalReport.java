package com.care.domain;

import com.care.domain.base.BaseModel;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import flexjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nujian on 16/3/20.
 */
@Entity
@SQLDelete(sql = "update medical_report_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class MedicalReport extends BaseModel{

    @Transient
    private List<Picture> pictures;

    public List<Picture> getPictures() {
        if(CollectionUtils.isNotEmpty(this.reportPictures)){
            List<Picture> pictures = new ArrayList<Picture>(Collections2.transform(this.getReportPictures(), new Function<MedicalReportPicture, Picture>() {
                @Override
                public Picture apply(MedicalReportPicture reportPicture) {
                    if(reportPicture.getPicture() != null){
                        return reportPicture.getPicture();
                    }
                    return null;
                }
            }));
        }
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private User nurse;


    @JSON(include = false)
    @OneToMany(mappedBy = "report")
    private List<MedicalReportPicture> reportPictures;

    //体温
    private BigDecimal temperature;

    //心率
    private BigDecimal heartRate;

    //肺活量()
    private BigDecimal fvc;

    //心脏杂音
    private Boolean heartMurmur;

    //是否白内障
    private Boolean cataract;

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

    public User getNurse() {
        return nurse;
    }

    public void setNurse(User nurse) {
        this.nurse = nurse;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(BigDecimal heartRate) {
        this.heartRate = heartRate;
    }

    public BigDecimal getFvc() {
        return fvc;
    }

    public void setFvc(BigDecimal fvc) {
        this.fvc = fvc;
    }

    public Boolean getHeartMurmur() {
        return heartMurmur;
    }

    public void setHeartMurmur(Boolean heartMurmur) {
        this.heartMurmur = heartMurmur;
    }

    public Boolean getCataract() {
        return cataract;
    }

    public void setCataract(Boolean cataract) {
        this.cataract = cataract;
    }

    public List<MedicalReportPicture> getReportPictures() {
        return reportPictures;
    }

    public void setReportPictures(List<MedicalReportPicture> reportPictures) {
        this.reportPictures = reportPictures;
    }
}
