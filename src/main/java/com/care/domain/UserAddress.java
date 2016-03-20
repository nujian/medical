package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.Gender;
import flexjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nujian on 16/2/26.
 */
@Configurable
@Entity
@SQLDelete(sql = "update user_address_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class UserAddress extends BaseModel {

    /**
     * 关联用户
     */
    @JSON(include = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Location location;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JSON(include = true)
    @Transient
    private String address;

    @JSON(include = false)
    @Column(name = "address_base",length = 200)
    private String addressBase;

    @JSON(include = false)
    @Column(name = "address_detail",length = 200)
    private String addressDetail;

    @Column(length = 50)
    private String contactName;

    @Column(length = 20)
    private String contactMobile;

    @JSON(include = false)
    public boolean isDefault = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        StringBuffer address = new StringBuffer();
        if(StringUtils.isNotBlank(this.getAddressBase())){
            address.append(this.getAddressBase());
        }
        if(StringUtils.isNotBlank(this.getAddressDetail())){
            address.append("_").append(this.getAddressDetail());
        }
        return address.toString();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getAddressBase() {
        return addressBase;
    }

    public void setAddressBase(String addressBase) {
        this.addressBase = addressBase;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public static UserAddress find(Integer addressId){
        return entityManager().find(UserAddress.class,addressId);
    }

    public static final UserAddress findUserDefaultAddress(User user){
        String query = "from UserAddress ua where ua.user.id=:user_id and ua.isDefault = true order by id desc";
        List<UserAddress> defaultAddress = entityManager().createQuery(query,UserAddress.class).setParameter("user_id",user.getId()).getResultList();
        return CollectionUtils.isNotEmpty(defaultAddress)?defaultAddress.get(0):null;
    }

    public static UserAddress getDisplayUserAddress(User user){
        UserAddress defaultAddress = findUserDefaultAddress(user);
        return defaultAddress != null?defaultAddress:CollectionUtils.isNotEmpty(user.getAddress())?user.getAddress().get(0):null;
    }
}
