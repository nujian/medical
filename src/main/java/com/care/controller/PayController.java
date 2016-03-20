package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.domain.Order;
import com.care.domain.User;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.PayChannel;
import com.care.service.OrderService;
import com.care.service.PayService;
import com.care.service.SecurityService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nujian on 16/2/23.
 */
@Controller
@RequestMapping(value = "/ws/pay")
@Api(name = "支付",description = "支付相关接口")
public class PayController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @ApiMethod(
            path = "/pay/send",verb = ApiVerb.POST,
            description = "发起支付",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiHeaders(headers = {@ApiHeader(name = "USER_LOGIN_TOKEN", description = "http请求header中用于验证用户的token") })
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public @ResponseBody
    @ApiResponseObject(sample = "")
    ResponseEntity<String> send(
            HttpServletResponse response, HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "orderId", description = "订单Id", required = true)
            @RequestParam(value = "orderId", required = true) Integer orderId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "payChannel", description = "支付渠道", required = true)
            @RequestParam(value = "payChannel", required = true) PayChannel payChannel,
            @ApiParam(paramType = ApiParamType.QUERY, name = "memo", description = "说明", required = false)
            @RequestParam(value = "memo", required = false) String memo
    ) throws Exception {
        User currentUser = securityService.getCurrentLoginUser(request);
//        Map<String,Object> result = payService.send(currentUser,request,response,orderId,payChannel,memo);
        Order order = orderService.updateStatus(currentUser,orderService.findOrder(orderId), OrderStatus.PAIED);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(order).toJson());
    }


}
