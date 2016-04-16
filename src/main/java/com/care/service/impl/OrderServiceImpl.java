package com.care.service.impl;

import com.care.domain.*;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.SortType;
import com.care.domain.enums.UserOrderType;
import com.care.domain.enums.UserType;
import com.care.exception.*;
import com.care.exception.base.CareException;
import com.care.service.OrderService;
import com.care.service.PictureService;
import com.care.utils.LocationUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nujian on 16/2/22.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private PictureService pictureService;

    @Override
    @Transactional
    public Order add(User user, Order order) throws CareException {
        Price price = Price.getLastPrice();
        if(price == null){
            throw new PriceInitException();
        }
        if(order.getAddress() == null){
            throw new CareException();
        }
        UserAddress address = UserAddress.find(order.getAddress().getId());
        Integer num = order.getNum()==null?1:order.getNum();
        Order targetOrder = new Order();
        targetOrder.setContactName(StringUtils.isBlank(address.getContactName())?user.getUsername():address.getContactName());
        targetOrder.setContactMobile(StringUtils.isBlank(address.getContactMobile()) ? user.getMobile() : address.getContactMobile());
        targetOrder.setUser(user);
        targetOrder.setStatus(OrderStatus.INIT);
        targetOrder.setTime(order.getTime());
        targetOrder.setAddress(address);
        targetOrder.setCost(price.getPrice().multiply(new BigDecimal(num)));
        targetOrder.setNum(num);
        targetOrder.setMemo(order.getMemo());
        targetOrder.persist();
        return targetOrder;
    }

    public Order findOrder(Integer orderId) {
        return findOrder(orderId, null);
    }

    @Override
    public Order findOrder(Integer orderId, User current) {
        Order order = Order.find(orderId);
        if(order != null && current != null){
            order.setUserType(getCurrentUserType(order,current));
            order.setAllowComment(checkAllowComment(order));
            order.setAllowCancel(checkAllowCancel(order));
        }
        return order;
    }

    private boolean checkAllowCancel(Order order) {
        boolean allowCancel = false;
        if(order.getUserType()!= null){
            switch (order.getUserType()){
                case USER:
                    allowCancel = (order.getNurse()==null) && !order.getStatus().equals(OrderStatus.FINISH);
                    break;
                case NURSE:
                    allowCancel = !order.getStatus().equals(OrderStatus.FINISH);
                    break;
            }
        }
        return allowCancel;
    }

    private boolean checkAllowComment(Order order) {
        boolean allowCancel = false;
        if(order.getStatus().equals(OrderStatus.CANCEL) || order.getStatus().equals(OrderStatus.FINISH)) {
            allowCancel = true;
        }else{
            if(CollectionUtils.isNotEmpty(OrderCancelLog.findLogByOrder(order))){
                allowCancel = true;
            }
        }
        return allowCancel;
    }


    private UserType getCurrentUserType(Order order,User user){
        UserType type = null;
        if(user.equals(order.getNurse())){
            type = UserType.NURSE;
        }
        if(user.equals(order.getUser())){
            type = UserType.USER;
        }
        return type;
    }

    public Order findOrderWithDistance(Integer orderId,Location location) {

        List<Order> orders = Arrays.asList(Order.find(orderId));
        if(CollectionUtils.isNotEmpty(orders)){
            orders = processOrderDistance(orders,location);
        }
        return CollectionUtils.isNotEmpty(orders)?orders.get(0):null;
    }

    @Override
    public List<Order> getOrderByUser(User user, Integer page, Integer count, Location location, UserOrderType userOrderType) {
        List<Order> orders = null;
        String query = makeQuery4getOrderByUser(user, userOrderType);
        orders = Order.entityManager().createQuery(query).setParameter("user_id",user.getId()).setFirstResult((page - 1) * count).setMaxResults(count).getResultList();
        if(location != null){
            orders = processOrderDistance(orders,location);
        }
        return orders;
    }

    private List<Order> processOrderDistance(List<Order> orders, Location location) {
        if(location != null){
            for(Order order:orders){
                UserAddress address = findAddressByOrder(order);//order.getAddress();
                if(address != null){
                    if(address.getLocation() != null){
                        order.setDistance(String.valueOf(LocationUtils.distance4km(location,address.getLocation()))+"KM");
                    }
                }
            }
        }
        return orders;
    }

    private UserAddress findAddressByOrder(Order order) {
        try{
            return order.getAddress();
        }catch (Exception e){
            return null;
        }
    }


    private String makeQuery4getOrderByUser(User user, UserOrderType userOrderType) {
        StringBuffer query = new StringBuffer();
        query.append("from Order o ");
        query.append("where ");
        boolean queryAccept = (userOrderType != null && userOrderType.equals(UserOrderType.ACCEPT)) || (userOrderType == null && user.getUserType().equals(UserType.NURSE));
        if(queryAccept){
            query.append(("o.nurse.id=:user_id "));
        }else{
            query.append("o.user.id=:user_id ");
        }
        query.append("order by o.id desc ");
        return query.toString();
    }

    @Override
    public List<Order> getOrderByStatus(List<OrderStatus> statuses, Integer page, Integer count, SortType sort, Location location) {
        List<Order> orders = null;
        String query = makeQuery4getOrderByStatus(sort);
        orders = Order.entityManager().createQuery(query,Order.class).setParameter("statuses",statuses)
                .setFirstResult((page - 1) * count).setMaxResults(count).getResultList();
        orders = processOrderDistance(orders,location);
        return orders;
    }

    private String makeQuery4getOrderByStatus(SortType sort) {
        StringBuffer query = new StringBuffer("from Order o ");
        query.append("where o.status in:statuses ");
        query.append("order by ");
        if(sort != null){
            switch (sort){
                case PRICE_ASC:
                    query.append("o.cost ,");
                    break;
                case PRICE_DESC:
                    query.append("o.cost desc,");
                    break;
                case TIME_ASC:
                    query.append("o.time ,");
                    break;
                case TIME_DESC:
                    query.append("o.time desc ,");
                    break;
                case DISTANCE_ASC:

                    break;
                case DISTANCE_DESC:

                    break;
                default:
                    query.append("o.time desc ,");
                    break;
            }
        }
        query.append("o.id desc ");
        return query.toString();
    }

    @Override
    @Transactional
    public Order match(Integer orderId, User nurse) throws CareException {
        Order order = findOrder(orderId);
        if(order == null){
            throw new EntityNotFoundException();
        }
        if(!nurse.getUserType().equals(UserType.NURSE)){
            throw new UserTypeErrorException();
        }
        if(!order.getStatus().equals(OrderStatus.PAIED)){
            throw new OrderStatusErrorException();
        }
        order.setNurse(nurse);
        order.merge();
        return order;
    }

    @Override
    @Transactional
    public Comment comment(User currentUser, Comment comment) throws Exception {
        Comment target = new Comment();
        Order order = findOrder(comment.getOrder().getId());
        if(order == null){
            throw new EntityNotFoundException();
        }
        target.setOrder(order);
        target.setUser(currentUser);
        if(StringUtils.isNotBlank(comment.getContent())){
            target.setContent(comment.getContent());
        }
        if(comment.getStar() != null){
            target.setStar(comment.getStar());
        }
        target.persist();
        if(CollectionUtils.isNotEmpty(comment.getPictures())){
            processCommentPictures(target,comment);
        }
        return target;
    }

    private void processCommentPictures(Comment target, Comment comment) throws Exception {
        List<byte[]> picByteArrayDataList = Picture.extractByteArrayDataFromPictureList(comment.getPictures());
        List<Picture> pictures = new ArrayList<Picture>();
        for (int i = 0; i < picByteArrayDataList.size(); i++) {
            pictures.add(pictureService.associate(target, picByteArrayDataList.get(i)));
        }
        target.setPictures(pictures);
        target.merge();
    }

    @Override
    public List<Comment> findCommentByOrderId(Integer orderId, Integer page, Integer count) {
        String query = "from Comment c where c.order.id=:order_id order by c.id desc";
        return Comment.entityManager().createQuery(query,Comment.class).setParameter("order_id",orderId)
                .setFirstResult((page - 1)* count).setMaxResults(count).getResultList();
    }

    @Override
    @Transactional
    public Order updateStatus(User user,Order order,OrderStatus status){
        Order target = findOrder(order.getId());
        target.setStatus(status);
        target.merge();
        return target;
    }

    public List<Order> findOrders(Integer page,Integer count){
        String query = "from Order o order by o.id desc";
        return Order.entityManager().createQuery(query,Order.class).setFirstResult((page -1)*count).setMaxResults(count).getResultList();
    }

    public Order cancel(Integer orderId, User currentUser, String memo) throws EntityNotFoundException, NoPermissionsException, OrderStatusErrorException {
        Order order = findOrder(orderId);
        boolean changed = false;
        if(order == null){
            throw new EntityNotFoundException();
        }
        if(currentUser.getUserType().equals(UserType.USER) && !order.getUser().equals(currentUser)){
            throw new NoPermissionsException();
        }
        if(currentUser.equals(order.getUser())){
            if(order.getNurse() != null){
                throw new OrderStatusErrorException();
            }
            order.setStatus(OrderStatus.CANCEL);
            changed = true;
        }else{
            if(order.getNurse() != null){
                if(!order.getNurse().equals(currentUser)){
                    throw new NoPermissionsException();
                }
                order.setNurse(null);
                order.setStatus(OrderStatus.PAIED);
                changed = true;
            }
        }
        logCancelOrder(currentUser,order,memo);
        if(changed){
            order.merge();
        }
        return order;
    }

    private void logCancelOrder(User user,Order order,String memo){
        OrderCancelLog log = new OrderCancelLog();
        log.setUser(user);
        log.setUserType(user.getUserType());
        log.setOrder(order);
        log.setMemo(memo);
        log.persist();
    }

    public MedicalReport uploadReport(User user,MedicalReport report){
        if(report.getId() != null){
            return MedicalReport.entityManager().find(MedicalReport.class,report.getId());
        }
        MedicalReport reportTarget = new MedicalReport();
        reportTarget.setCataract(report.getCataract());
        reportTarget.setHeartMurmur(report.getHeartMurmur());
        reportTarget.setFvc(report.getFvc());
        reportTarget.setTemperature(report.getTemperature());
        Order order = report.getOrder();
        if(order != null){
            reportTarget.setNurse(order.getNurse());
            reportTarget.setUser(order.getUser());
            reportTarget.setOrder(order);
        }
        reportTarget.persist();
        if(CollectionUtils.isNotEmpty(report.getPictures())){
            List<byte[]> picByteArrayDataList = Picture.extractByteArrayDataFromPictureList(report.getPictures());
            for (int i = 0; i < picByteArrayDataList.size(); i++) {
                MedicalReportPicture reportPicture = new MedicalReportPicture();
                reportPicture.setReport(reportTarget);
                reportPicture.persist();
                try {
                    reportPicture.setPicture(pictureService.associate(reportPicture, picByteArrayDataList.get(i)));
                    reportPicture.merge();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return reportTarget;
    }

    @Override
    public MedicalReport findReport(Integer id) {
        return MedicalReport.entityManager().find(MedicalReport.class,id);
    }
}
