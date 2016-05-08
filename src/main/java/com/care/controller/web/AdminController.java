package com.care.controller.web;

import com.care.domain.User;
import com.care.service.OrderService;
import com.care.service.StatisticsService;
import com.care.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nujian on 16/3/4.
 */
@Controller
@RequestMapping(value = "/web/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/index/{mainType}" ,method = RequestMethod.GET)
    public String index(HttpSession session,Model model,@PathVariable("mainType") Integer type){
        if(session.getAttribute("user")==null){
            return "admin/login";
        }
        final Integer initPage = 1;
        final Integer initCount = 10;
        Integer totalPage = null;
        if(type != null){
            model.addAttribute("mainType",type);
            switch (type){
                case 1:
                    model.addAttribute("users",userService.findUsers(initPage,initCount));
                    totalPage = userService.findUserTotalPage(initCount);
                    break;
                case 2:
                    model.addAttribute("orders",orderService.findOrders(initPage,initCount));
                    totalPage = orderService.findOrderTotalPage(initCount);
                    break;
                case 3:
                    model.addAttribute("cashes",userService.findUnhandelUserCashLog(initPage,initCount));
                    totalPage = userService.findCashTotalPage(initCount);
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("statistics",statisticsService.getStatisticsDataByType(type));
        return "admin/main";
    }

    @RequestMapping(value = "/index/{mainType}/{page}" ,method = RequestMethod.GET)
    public String index(HttpSession session,Model model, @PathVariable("mainType") Integer type,
                        @PathVariable("page") Integer page){
        if(session.getAttribute("user")==null){
            return "admin/login";
        }
        final Integer initCount = 10;
        Integer totalPage = null;
        if(type != null){
            model.addAttribute("mainType",type);
            switch (type){
                case 1:
                    model.addAttribute("users",userService.findUsers(page,initCount));
                    totalPage = userService.findUserTotalPage(initCount);
                    break;
                case 2:
                    model.addAttribute("orders",orderService.findOrders(page,initCount));
                    totalPage = orderService.findOrderTotalPage(initCount);
                    break;
                case 3:
                    model.addAttribute("cashes",userService.findUnhandelUserCashLog(page,initCount));
                    totalPage = userService.findCashTotalPage(initCount);
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("statistics",statisticsService.getStatisticsDataByType(type));
        return "admin/main";
    }


    @RequestMapping(value = "/index/user/stop/{userId}/{mainType}" ,method = RequestMethod.GET)
    public String stop(HttpSession session,Model model,
                        @PathVariable("userId") Integer userId,
                        @PathVariable("mainType") Integer type){
        if(session.getAttribute("user")==null){
            return "admin/login";
        }
        userService.stopUser(userId);
        final Integer page = 1;
        final Integer initCount = 10;
        Integer totalPage = null;
        if(type != null){
            model.addAttribute("mainType",type);
            switch (type){
                case 1:
                    model.addAttribute("users",userService.findUsers(page,initCount));
                    totalPage = userService.findUserTotalPage(initCount);
                    break;
                case 2:
                    model.addAttribute("orders",orderService.findOrders(page,initCount));
                    totalPage = orderService.findOrderTotalPage(initCount);
                    break;
                case 3:
                    model.addAttribute("cashes",userService.findUnhandelUserCashLog(page,initCount));
                    totalPage = userService.findCashTotalPage(initCount);
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("statistics",statisticsService.getStatisticsDataByType(type));
        return "admin/main";
    }

    @RequestMapping(value = "/index/user/unStop/{userId}/{mainType}" ,method = RequestMethod.GET)
    public String unStop(HttpSession session,Model model,
                       @PathVariable("userId") Integer userId,
                       @PathVariable("mainType") Integer type){
        if(session.getAttribute("user")==null){
            return "admin/login";
        }
        userService.unStopUser(userId);
        final Integer page = 1;
        final Integer initCount = 10;
        Integer totalPage = null;
        if(type != null){
            model.addAttribute("mainType",type);
            switch (type){
                case 1:
                    model.addAttribute("users",userService.findUsers(page,initCount));
                    totalPage = userService.findUserTotalPage(initCount);
                    break;
                case 2:
                    model.addAttribute("orders",orderService.findOrders(page,initCount));
                    totalPage = orderService.findOrderTotalPage(initCount);
                    break;
                case 3:
                    model.addAttribute("cashes",userService.findUnhandelUserCashLog(page,initCount));
                    totalPage = userService.findCashTotalPage(initCount);
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("statistics",statisticsService.getStatisticsDataByType(type));
        return "admin/main";
    }


    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public String login(User user,HttpSession session,Model model){
        if(StringUtils.isNotBlank(user.getMobile())){
            User target = User.findUserByMobile(user.getMobile());
            if(target != null && target.getPassword().equals(DigestUtils.sha256Hex(user.getPassword()))){
                final Integer initPage = 1;
                final Integer initCount = 10;
                session.setAttribute("user", target);
                model.addAttribute("user",target);
                model.addAttribute("orders",orderService.findOrders(initPage,initCount));
                model.addAttribute("users",userService.findUsers(initPage,initCount));
                model.addAttribute("cashes",userService.findUnhandelUserCashLog(initPage,initCount));
                return "admin/main";
            }
        }
        return "admin/login";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){
        if(session.getAttribute("user") != null){
            session.setAttribute("user",null);
        }
        return "admin/login";
    }
}
