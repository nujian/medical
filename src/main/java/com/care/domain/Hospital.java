package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.embeddables.Location;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

/*
 * Created by nujian on 16/2/22.
 */
@Entity
@SQLDelete(sql = "update hospital_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Hospital extends BaseModel {

    @Column(length = 120)
    @Index(name = "name")
    private String name;

    @Column(length = 100)
    private String level;

    @Column(columnDefinition = "text")
    private String address;

    @Embedded
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
