package com.care.controller.interceptors;

import com.care.domain.ApiInvokeLog;
import com.care.domain.User;
import com.care.domain.embeddables.Location;
import com.care.service.SecurityService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by nujian on 16/2/17.
 */
public class ApiInvokeIntercepor implements HandlerInterceptor {
    private static final String blank = " ";
    private static final String fsep = "\"";
    private static final Logger logger = Logger.getLogger(ApiInvokeIntercepor.class);
    private ThreadLocal<Long> start = new ThreadLocal<Long>();

    @Autowired
    private transient MailSender mailSender;

    @Autowired
    private transient SimpleMailMessage exceptionMessage;


    @Autowired
    private SecurityService securityService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

//        logger.info("start call :" + getRequestInvokeLog(request));
        start.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        long cost = System.currentTimeMillis() - start.get();
        StringBuffer s = new StringBuffer();
        User user = null;
        try {
            user = securityService.getCurrentLoginUser(request);//getAuthenticatedUserId(request);
        }catch(Exception ee){}
        s.append(fsep);
        s.append(request.getHeader("user-agent"));
        s.append(fsep);
        s.append(blank);


        s.append(fsep);
        s.append(request.getMethod());
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append("userId:"+(user==null?null:user.getId()));
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(ip);
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append("ip:"+request.getRemoteHost());
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(url);
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append("cost:").append(cost);
        s.append(fsep);
        s.append(blank);


        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        for (String param : paramMap.keySet()) {
            s.append(fsep);
            s.append(param).append(":");
            params += param + ":";
            for (String value : paramMap.get(param)) {
                s.append(value).append(blank);
                params += value + " ";
            }
            params += "\n";
            s.append(fsep);
            s.append(blank);
        }
        logger.info(s);
        System.out.println(s.toString());

        try {
            ApiInvokeLog invokeLog = new ApiInvokeLog();
            if(user != null){
                invokeLog.setUser(user);
            }
            invokeLog.setAction(url);
            invokeLog.setCostTime(cost);
            invokeLog.setLocation(parseLocationFromRequest(request));
            invokeLog.setUserAgent(request.getHeader("user-agent"));
            invokeLog.persist();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private Integer getAuthenticatedUserId(HttpServletRequest request) {
        try {
            return User.findUserByMobile(request.getUserPrincipal().getName()).getId();
        } catch (Exception e) {
            return null;
        }
    }

    private User getAuthenticatedUser(HttpServletRequest request) {
        try {
            return User.findUserByMobile(request.getUserPrincipal().getName());
        } catch (Exception e) {
            return null;
        }
    }

    private Location parseLocationFromRequest(HttpServletRequest request) {
        try {
            Double latitude = Double.parseDouble(request.getParameter("latitude"));
            Double longitude = Double.parseDouble(request.getParameter("longitude"));
            return Location.fromCoordinate(latitude, longitude);
        } catch (Exception e) {
            return Location.fromCoordinate(0.0, 0.0);
        }
    }

    private String getRequestInvokeLog(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        StringBuffer s = new StringBuffer();

        Integer userId = getAuthenticatedUserId(request);
        s.append(fsep);
        s.append(request.getHeader("user-agent"));
        s.append(fsep);
        s.append(blank);


        s.append(fsep);
        s.append(request.getMethod());
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(userId);
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(ip);
        s.append(fsep);
        s.append(blank);


        s.append(fsep);
        s.append(url);
        s.append(fsep);
        s.append(blank);


        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        for (String param : paramMap.keySet()) {
            s.append(fsep);
            s.append(param).append(":");
            params += param + ":";
            for (String value : paramMap.get(param)) {
                s.append(value).append(blank);
                params += value + " ";
            }
            params += "\n";
            s.append(fsep);
            s.append(blank);
        }
        return s.toString();
    }


}
