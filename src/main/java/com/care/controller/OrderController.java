package com.care.controller;

import com.care.controller.result.ResponseEntityUtils;
import com.care.controller.result.ResultBean;
import com.care.domain.Comment;
import com.care.domain.MedicalReport;
import com.care.domain.Order;
import com.care.domain.User;
import com.care.domain.embeddables.Location;
import com.care.domain.enums.OrderStatus;
import com.care.domain.enums.SortType;
import com.care.domain.enums.UserOrderType;
import com.care.exception.base.CareException;
import com.care.service.OrderService;
import com.care.service.SecurityService;
import com.care.utils.PageCountUtils;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nujian on 16/2/22.
 */
@Controller
@RequestMapping(value = "/ws/orders")
@Api(name = "订单",description = "订单相关接口")
public class OrderController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrderService orderService;


    @ApiMethod(
            path = "/orders/add",
            verb = ApiVerb.POST,
            description = "下单",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> add(
            HttpServletRequest request,
            Order order,
            @ApiParam(paramType = ApiParamType.QUERY,name = "contactName",description = "联系人姓名",required = true)
            @RequestParam(value = "contactName",required = true)String contactName,
            @ApiParam(paramType = ApiParamType.QUERY,name = "contactMobile",description = "联系人手机号",required = true)
            @RequestParam(value = "contactMobile",required = true)String contactMobile,
            @ApiParam(paramType = ApiParamType.QUERY,name = "address.id",description = "服务地址(选择用户的地址，若没有先创建，再选择)",required = true)
            @RequestParam(value = "address.id",required = true)String address,
            @ApiParam(paramType = ApiParamType.QUERY,name = "time",description = "服务时间(yyyy-MM-dd HH:mm)",required = true)
            @RequestParam(value = "time",required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") String time,
            @ApiParam(paramType = ApiParamType.QUERY,name = "num",description = "体检人数",required = true)
            @RequestParam(value = "num",required = false) String num,
            @ApiParam(paramType = ApiParamType.QUERY,name = "memo",description = "留言",required = true)
            @RequestParam(value = "memo",required = false) String memo
    ) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orderService.add(current, order),new String[]{"addressPic"}).toJson());
    }

    @ApiMethod(
            path = "/orders/{id}",
            verb = ApiVerb.GET,
            description = "订单详情",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> add(@PathVariable("id") Integer id,HttpServletRequest request) throws CareException {
        User current = securityService.getCurrentLoginUser(request);
        String[] inclends = new String[]{"user","nurse","addressPic","comments"};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orderService.findOrder(id, current),inclends).toJson());
    }

    @ApiMethod(
            path = "/orders/getMyOrders",
            verb = ApiVerb.GET,
            description = "加载我的订单",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/getMyOrders", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> getMyOrders(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false)
            @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)
            @RequestParam(value = "count",required = false) Integer count,
            Location location,
            @ApiParam(paramType = ApiParamType.QUERY, name = "longitude", description = "经度", required = false)
            @RequestParam(value = "longitude",required = false) Double longitude,
            @ApiParam(paramType = ApiParamType.QUERY, name = "latitude", description = "纬度", required = false)
            @RequestParam(value = "latitude",required = false) Double latitude,
            @ApiParam(paramType = ApiParamType.QUERY, name = "userOrderType", description = "用户订单类型", required = false,allowedvalues = {"ADD:我下的订单","ACCEPT:我接的订单"})
            @RequestParam(value = "userOrderType",required = false) UserOrderType userOrderType
    ) throws CareException {
        User currentUser = securityService.getCurrentLoginUser(request);
        List<Order> orders = orderService.getOrderByUser(currentUser, PageCountUtils.processPage(page), PageCountUtils.processCount(count), location, userOrderType);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orders,new String[]{"*.addressPic"}).toJson());
    }

    @ApiMethod(
            path = "/orders/getUnMatchedOrders",
            verb = ApiVerb.GET,
            description = "加载未匹配的订单",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/getUnMatchedOrders", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> getMyOrders(
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false) @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)  @RequestParam(value = "count",required = false) Integer count,
            Location location,
            @ApiParam(paramType = ApiParamType.QUERY, name = "longitude", description = "经度", required = false)
            @RequestParam(value = "longitude",required = false) Double longitude,
            @ApiParam(paramType = ApiParamType.QUERY, name = "latitude", description = "单页返回的记录条数", required = false)
            @RequestParam(value = "latitude",required = false) Double latitude
    ) throws CareException {
        List<Order> orders = orderService.getOrderByStatus(Arrays.asList(OrderStatus.PAIED), PageCountUtils.processPage(page), PageCountUtils.processCount(count), SortType.TIME_DESC, location);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orders,new String[]{"*.addressPic"}).toJson());
    }

    @ApiMethod(
            path = "/orders/match",
            verb = ApiVerb.POST,
            description = "抢单",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/match", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> match(HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "orderId", description = "订单Id", required = false)
            @RequestParam(value = "orderId",required = false) Integer orderId
    ) throws CareException {
        User currentUser = securityService.getCurrentLoginUser(request);
        String[] inclends = new String[]{"addressPic","nurse"};
        String[] exclends = new String[]{""};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orderService.match(orderId,currentUser),inclends,exclends).toJson());
    }

    @ApiMethod(
            path = "/orders/cancel",
            verb = ApiVerb.POST,
            description = "取消",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> cancel(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "orderId", description = "订单Id", required = true)
            @RequestParam(value = "orderId",required = true) Integer orderId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "memo", description = "取消原因", required = false)
            @RequestParam(value = "memo",required = false) String memo
    ) throws CareException {
        User currentUser = securityService.getCurrentLoginUser(request);
        String[] inclends = new String[]{"addressPic","nurse"};
        String[] exclends = new String[]{""};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orderService.cancel(orderId, currentUser, memo),inclends,exclends).toJson());
    }

    @ApiMethod(
            path = "/orders/comment",
            verb = ApiVerb.POST,
            description = "评论",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> comment(
            HttpServletRequest request,
            Comment comment,
            @ApiParam(paramType = ApiParamType.QUERY, name = "order.id", description = "订单Id", required = true)
            @RequestParam(value = "order.id",required = true) Integer orderId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "star", description = "订单星级", required = false)
            @RequestParam(value = "star",required = false) String star,
            @ApiParam(paramType = ApiParamType.QUERY, name = "content", description = "评论图片", required = false)
            @RequestParam(value = "content",required = false) String content,
            @ApiParam(paramType = ApiParamType.QUERY, name = "pictures[].file", description = "评论图片", required = false)
            @RequestParam(value = "pictures[].file",required = false) String pictures
    ) throws Exception {
        User currentUser = securityService.getCurrentLoginUser(request);
        Comment result = orderService.comment(currentUser, comment);
        String[] inclends = new String[]{"order.id"};
        String[] exclends = new String[]{"order.*"};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(result,inclends,exclends).toJson());
    }

    @ApiMethod(
            path = "/orders/comment/list",
            verb = ApiVerb.GET,
            description = "订单评论",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @RequestMapping(value = "/comment/list", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> commentList(
            HttpServletRequest request,
            @ApiParam(paramType = ApiParamType.QUERY, name = "orderId", description = "订单Id", required = true)
            @RequestParam(value = "orderId",required = true) Integer orderId,
            @ApiParam(paramType = ApiParamType.QUERY, name = "page", description = "分页页码", required = false)
            @RequestParam(value = "page",required = false) Integer page,
            @ApiParam(paramType = ApiParamType.QUERY, name = "count", description = "单页返回的记录条数", required = false)
            @RequestParam(value = "count",required = false) Integer count
    ) throws Exception {
        List<Comment> comments = orderService.findCommentByOrderId(orderId, PageCountUtils.processPage(page), PageCountUtils.processCount(count));
        String[] inclends = new String[]{"order.id"};
        String[] exclends = new String[]{"order.*"};
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(comments,inclends,exclends).toJson());
    }

    @ApiMethod(
            path = "/orders/report/upload",
            verb = ApiVerb.POST,
            description = "上传体检报告",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ApiHeaders(headers = {
            @ApiHeader(name = "USER_LOGIN_TOKEN", description = "检测用户登陆的token")
    })
    @RequestMapping(value = "/report/upload", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponseObject(sample = "")
    public ResponseEntity<String> commentList(
            HttpServletRequest request,
            MedicalReport report,
            @ApiParam(paramType = ApiParamType.QUERY, name = "temperature", description = "体温", required = true)
            @RequestParam(value = "temperature",required = true) String temperature,
            @ApiParam(paramType = ApiParamType.QUERY, name = "heartRate", description = "心率", required = true)
            @RequestParam(value = "heartRate",required = true) String heartRate,
            @ApiParam(paramType = ApiParamType.QUERY, name = "fvc", description = "肺活量", required = true)
            @RequestParam(value = "fvc",required = true) String fvc,
            @ApiParam(paramType = ApiParamType.QUERY, name = "heartMurmur", description = "心脏杂音(T/F)", required = true)
            @RequestParam(value = "heartMurmur",required = true) String heartMurmur,
            @ApiParam(paramType = ApiParamType.QUERY, name = "cataract", description = "白内障(T/F)", required = true)
            @RequestParam(value = "cataract",required = true) String cataract,
            @ApiParam(paramType = ApiParamType.QUERY, name = "pictures[].file", description = "报告图片", required = false)
            @RequestParam(value = "pictures[].file",required = false) String reportPic,
            //订单Id
            @ApiParam(paramType = ApiParamType.QUERY, name = "orderId", description = "订单Id", required = true)
            @RequestParam(value = "orderId",required = true) Integer orderId
            ) throws Exception {
        if(orderId != null){
            report.setOrder(orderService.findOrder(orderId));
        }
        User nurse = securityService.getCurrentLoginUser(request);
        return ResponseEntityUtils.wrapResponseEntity(ResultBean.wrap(orderService.uploadReport(nurse,report)).toJson());
    }

}
