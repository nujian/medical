package com.care.domain;

import com.care.Constants;
import com.care.domain.base.BaseModel;
import com.care.domain.enums.PictureType;
import flexjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nujian on 16/2/19.
 */
@Entity
@SQLDelete(sql = "update picture_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Picture extends BaseModel {

    public static final String PIC_280_280 = "_280_280";

    @JSON(include = false)
    @Column(name = "ref_id")
    @NotNull
    private Integer refId;

    @JSON(include = false)
    @Column(name = "DTYPE")
    @NotNull
    private String dtype;


    @JSON(include = false)
    @Enumerated(EnumType.STRING)
    private PictureType type;


    @JSON(include = false)
    private String src;


    @JsonIgnore
    @JSON(include = false)
    @Transient
    private MultipartFile file;

    @JSON(include = true)
    @Transient
    public String accessPath;


    @Transient
    @JSON(name = "url_280_280")
    public String getUrl_280_280() {
        if(StringUtils.isNotBlank(src)){
            return Constants.images_access_baseurl + src+PIC_280_280+".jpg";
        }
        return null;
    }


    public String getAccessPath() {
        if(StringUtils.isNotBlank(src)){
            return Constants.images_access_baseurl+src+".jpg";
        }
        return null;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }


    public static List<byte[]> extractByteArrayDataFromPictureList(List<Picture> pictureList)  {
        List<byte[]> picByteArrayDataList = new ArrayList<byte[]>();
        try {
            if (CollectionUtils.isEmpty(pictureList))
                return Collections.emptyList();
            for (Picture p : pictureList) {
                if (p.getFile().getBytes() != null && p.getFile().getBytes().length > 0) {
                    picByteArrayDataList.add(p.getFile().getBytes());
                } else if (p.getFile() != null) {
                    try {
                        picByteArrayDataList.add(p.getFile().getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return picByteArrayDataList;
    }
}
