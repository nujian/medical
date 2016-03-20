package com.care.service;

import com.care.domain.User;
import com.care.domain.enums.PayChannel;
import com.care.exception.EntityNotFoundException;
import com.care.exception.OrderStatusErrorException;
import org.omg.CORBA.portable.ResponseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by nujian on 16/2/23.
 */
public interface PayService {

    Map<String,Object> send(User user, HttpServletRequest request, HttpServletResponse response, Integer orderId, PayChannel payChannel, String memo) throws EntityNotFoundException, OrderStatusErrorException;

    /**
     * 支付宝通知
     * @param request
     * @return
     */
    boolean notifyAliPay(HttpServletRequest request) throws EntityNotFoundException;

    /**
     * 微信支付通知
     * responseHandler
     * @throws Exception
     */
    boolean notifyWeixin(ResponseHandler responseHandler) throws Exception;
}
