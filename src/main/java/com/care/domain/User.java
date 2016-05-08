package com.care.domain;

import com.care.domain.base.BaseModel;
import com.care.domain.enums.Gender;
import com.care.domain.enums.PictureType;
import com.care.domain.enums.UserStatus;
import com.care.domain.enums.UserType;
import com.care.utils.DateUtils;
import flexjson.JSON;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by nujian on 16/2/17.
 */
@Configurable
@Entity
@SQLDelete(sql = "update user_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class User extends BaseModel {

    public static final PictureType PICTURE_TYPE = PictureType.USER_PORTRAIT;

    @Column(name = "username",length = 100)
    private String username;

    @Column(name = "personalized_sign",length = 200)
    private String personalizedSign;

    @JSON(include = false)
    @Column(name = "mobile",unique = true,length = 20)
    @NotNull
    @Index(name = "mobile")
    private String mobile;

    @JSON(include = false)
    @Column(name = "password",length = 100)
    @NotNull
    private String password;

    @JSON(include = false)
    @Transient
    private String verifyCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Index(name = "birth_date")
    private Date birthDate;

    @Index(name = "user_type")
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @JSON(include = true)
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @JSON(include = false)
    @OneToOne(mappedBy = "user")
    private UserWallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portrait_id")
    private Picture portrait;

    @Transient
    @JSON(include = true)
    public Integer getAge() {
        if (birthDate != null) {
            return DateUtils.diffYear(new Date(), birthDate);
        }
        return null;
    }

    @OneToOne(mappedBy = "user")
    private NurseInfo nurseInfo;

    @JSON(include = false)
    @OneToMany(mappedBy = "user")
    private List<UserAddress> address;

    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.NORMAL;

    @JSON(include = false)
    @Transient
    private UserAddress defaultAddress;

    public UserAddress getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(UserAddress defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public UserWallet getWallet() {
        return wallet;
    }

    public void setWallet(UserWallet wallet) {
        this.wallet = wallet;
    }

    public Picture getPortrait() {
        return portrait;
    }

    public void setPortrait(Picture portrait) {
        this.portrait = portrait;
    }

    public NurseInfo getNurseInfo() {
        return nurseInfo;
    }

    public void setNurseInfo(NurseInfo nurseInfo) {
        this.nurseInfo = nurseInfo;
    }

    public String getPersonalizedSign() {
        return personalizedSign;
    }

    public void setPersonalizedSign(String personalizedSign) {
        this.personalizedSign = personalizedSign;
    }

    public List<UserAddress> getAddress() {
        return address;
    }

    public void setAddress(List<UserAddress> address) {
        this.address = address;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public static User findUserByMobile(String mobile){
        User user = null;
        String query = "from User u where u.mobile=:mobile";
        try{
            user = entityManager().createQuery(query,User.class).setParameter("mobile",mobile).getSingleResult();
        }catch(Exception e){
//            e.printStackTrace();
        }
        return user;
    }

    public static User findUser(Integer userId) {
        return entityManager().find(User.class,userId);
    }
}
