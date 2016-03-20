package com.care.controller.webinvoke;

import com.care.domain.ApiInvokeLog;
import com.care.service.SecurityService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Created by user on 2015/2/11.
 */
public class WebInvokeInterceptor implements HandlerInterceptor {

    @Autowired
    private SecurityService securityService;

    private static final String blank = " ";
    private static final String fsep = "\"";

    private ThreadLocal<Long> start = new ThreadLocal<Long>();
    private static final Logger logger = Logger.getLogger(WebInvokeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        start.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        User user = securityService.getCurrentLoginUser(request);
        try {
//            if (user != null) {
//                modelAndView.addObject("appVersion", logService.getLastLoginClientVersionByUserId(user.getId()));
//            }
//            String userAgent = request.getHeader("User-Agent");
//            if(StringUtils.isNotBlank(userAgent)){
//                modelAndView.addObject("isWeixin", CommonUtils.isWeixinWeb(userAgent));
//                modelAndView.addObject("isIos", CommonUtils.isIosWebClient(userAgent));
//            }
        } catch (Exception e) {
//            e.printStackTrace();
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();

        long cost = System.currentTimeMillis() - start.get();
        StringBuffer s = new StringBuffer();

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
//        s.append(userId);
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(ip);
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(request.getRemoteHost());
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
        //System.out.println(s.toString());

        try {
            ApiInvokeLog invokeLog = new ApiInvokeLog();
//            if (userId != null && userId > 0) {
//                invokeLog.setUser(User.findUser(userId));
//            }
//            invokeLog.setAction(url);
//            invokeLog.setCostTime(cost);
//            invokeLog.setLocation(parseLocationFromRequest(request));
//            logService.logApiInvokation(invokeLog);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
