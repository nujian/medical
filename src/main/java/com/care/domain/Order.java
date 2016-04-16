package com.care.domain;

import com.care.Constants;
import com.care.domain.base.BaseModel;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.UserType;
import com.care.utils.LocationUtils;
import flexjson.JSON;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by nujian on 16/2/22.
 */
@Entity
@SQLDelete(sql = "update order_ set is_deleted=1,update_time=NOW() where id=? and version=? ")
@Where(clause = "is_deleted=0")
public class Order extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 100)
    @Index(name = "contact_name")
    private String contactName;

    @Column(length = 20)
    @Index(name = "contact_mobile")
    private String contactMobile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @Where(clause = "where is_deleted = 0")
    private UserAddress address;

    @Index(name = "status")
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.INIT;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    @Index(name = "time")
    private Date time;

    @JSON(include = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private User nurse;

    private BigDecimal cost;

    private Integer num;

    @Column(columnDefinition = "text")
    private String memo;

    @JSON
    @Transient
    private String distance;

    @Transient
    private UserType userType;

    @Transient
    private boolean allowComment;

    @Transient
    private boolean allowCancel;

    @JSON(include = false)
    @OneToMany(mappedBy = "order")
    private List<Comment> comments;

    @JSON(include = false,name = "addressPic")
    @Transient
    private String getAddressPic(){
        if(this.getAddress() != null && this.getAddress().getLocation() != null){
            return LocationUtils.getLocationPic(this.getAddress().getLocation(),
                    Constants.ORDER_ADDRESS_PIC_WIDTH,
                    Constants.ORDER_ADDRESS_PIC_HEIGHT,
                    Constants.ORDER_ADDRESS_PIC_ZOOM);
        }
        return null;
    }

    @Transient
    private MedicalReport report;

    public MedicalReport getReport() {
        return MedicalReport.getReportByOrderId(this.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getNurse() {
        return nurse;
    }

    public void setNurse(User nurse) {
        this.nurse = nurse;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public static Order find(Integer orderId){
        return entityManager().find(Order.class,orderId);
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isAllowComment() {
        return allowComment;
    }

    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }

    public boolean isAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(boolean allowCancel) {
        this.allowCancel = allowCancel;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
