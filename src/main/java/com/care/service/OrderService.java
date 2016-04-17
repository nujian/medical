package com.care.service;

import com.care.domain.Comment;
import com.care.domain.MedicalReport;
import com.care.domain.Order;
import com.care.domain.User;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.SortType;
import com.care.domain.enums.UserOrderType;
import com.care.exception.EntityNotFoundException;
import com.care.exception.NoPermissionsException;
import com.care.exception.OrderStatusErrorException;
import com.care.exception.base.CareException;

import java.util.List;

/**
 * Created by nujian on 16/2/22.
 */
public interface OrderService {

    Order add(User user, Order order) throws CareException;

    Order findOrder(Integer orderId);

    Order findOrder(Integer orderId, User current);

    List<Order> getOrderByUser(User user, Integer page, Integer count, Location location, UserOrderType userOrderType);

    List<Order> getOrderByStatus(List<OrderStatus> statuses,boolean matched, Integer page, Integer count, SortType sort, Location location);

    Order match(Integer orderId, User nurse) throws CareException;

    Comment comment(User currentUser, Comment comment) throws Exception;

    List<Comment> findCommentByOrderId(Integer orderId, Integer page, Integer count);

    Order updateStatus(User user, Order order, OrderStatus targetStatus);

    //后台管理相关
    List<Order> findOrders(Integer page, Integer count);

    Order cancel(Integer orderId, User currentUser, String memo) throws EntityNotFoundException, NoPermissionsException, OrderStatusErrorException;

    //上传体检报告
    MedicalReport uploadReport(User user,MedicalReport report);

    MedicalReport findReport(Integer id);
}
