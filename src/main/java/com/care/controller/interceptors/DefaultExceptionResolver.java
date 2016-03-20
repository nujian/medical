package com.care.controller.interceptors;


import com.care.Constants;
import com.care.controller.result.ResultBean;
import com.care.exception.base.CareException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by nujian on 2015/5/28.
 */
public class DefaultExceptionResolver extends DefaultHandlerExceptionResolver {
    @Autowired
    private transient MailSender mailSender;


    @Autowired
    private transient SimpleMailMessage exceptionMessage;


    private static final String blank = " ";
    private static final String fsep = "\"";

    @Value("${exception.notify.mails}")
    private String exceptionRecvMails;

    @Override
    protected void sendServerError(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        e.printStackTrace();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        String host = request.getLocalAddr();
        StringBuffer s = new StringBuffer();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            s.append(fsep);
            s.append(headerKey + ": " + request.getHeader(headerKey));
            s.append(fsep);
            s.append(blank);
            s.append("\n");
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


        String errorBody = "";
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        ResultBean result = new ResultBean();
        if (e instanceof CareException) {
            result.setCode(String.valueOf(((CareException) e).getCode()));
            errorBody = ((CareException) e).getErrorBody();
        } else {
            result.setCode(String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
        if (!ArrayUtils.contains(Constants.EXCLUDE_NOTIFY_EXPCETIONS, e.getClass())) {
//            messageService.notifyBackendAdmin(null, exceptionRecvMails, e.getMessage() + " " + host, s.toString() + "\n" + ExceptionUtils.getStackTrace(e) + "\n" + errorBody);
        }


        result.setMsg(e.getMessage());
        try {
            response.getWriter().println(result.toJson());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
