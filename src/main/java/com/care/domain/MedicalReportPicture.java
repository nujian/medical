package com.care.domain;

import com.care.domain.base.BaseModel;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

/**
 * Created by nujian on 16/4/16.
 */
@Entity
@SQLDelete(sql = "update medical_report_picture_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class MedicalReportPicture extends BaseModel{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private MedicalReport report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    public MedicalReport getReport() {
        return report;
    }

    public void setReport(MedicalReport report) {
        this.report = report;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public static List<MedicalReportPicture> findReportPictures(Integer reportId){
        String query = "from MedicalReportPicture mrp where mrp.report.id=:report_id order by mrp.createTime desc,mrp.id desc";
        List<MedicalReportPicture> reportPictures = entityManager().createQuery(query,MedicalReportPicture.class)
                .setParameter("report_id",reportId).getResultList();
        return reportPictures;
    }
}
