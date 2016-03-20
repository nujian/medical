package com.care.controller.interceptors;


import com.care.Constants;
import com.care.controller.result.ResultBean;
import com.care.domain.User;
import com.care.exception.base.CareException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;


/**
 * Created by nujian on 16/2/17.
 */
public class ExceptionResolver implements HandlerExceptionResolver {
    @Autowired
    private transient MailSender mailSender;

    @Autowired
    private transient SimpleMailMessage exceptionMessage;

//    @Autowired
//    private SecurityService securityService;

    private static final String blank = " ";
    private static final String fsep = "\"";

    @Value("${exception.notify.mails}")
    private String exceptionRecvMails;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        e.printStackTrace();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        String host = request.getLocalAddr();
        StringBuffer s = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();

        s.append("======== REQUEST HEADERS ======== \n");
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            s.append(fsep);
            s.append(headerKey + ": " + request.getHeader(headerKey));
            s.append(fsep);
            s.append(blank);
            s.append("\n");
        }
        s.append("======== REQUEST PARAMETER ======== \n");
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
        s.append(fsep);
        s.append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(request.getLocalAddr());
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(request.getMethod());
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
        s.append(request.getHeader("user-agent"));
        s.append(fsep);
        s.append(blank);

        s.append(fsep);
//        s.append(securityService.getCurrentRequestUserId(request));
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

        String errorBody = "";
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ResultBean result = new ResultBean();
        String code = null;
        String message = "";
        if (e instanceof CareException) {
            code = String.valueOf(((CareException) e).getCode());
            result.setCode(code);
//            errorBody = ((CareException) e).getErrorBody();
            if(StringUtils.isNotBlank(((CareException) e).getMemo())){
                message = ((CareException) e).getMemo();
            }else{
                message = e.getMessage();
            }
        } else {
            code = String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.setCode(code);
            message = e.getMessage();
        }

        if (!ArrayUtils.contains(Constants.EXCLUDE_NOTIFY_EXPCETIONS, e.getClass())) {
            //todo 通知后台管理人员
        }


        result.setMsg(message);
        try {
            response.getWriter().println(result.toJson());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView();
    }

    private Integer getAuthenticatedUserId(HttpServletRequest request) {
        try {
            return User.findUserByMobile(request.getUserPrincipal().getName()).getId();
        } catch (Exception e) {
            return 0;
        }
    }
}
