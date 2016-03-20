package com.care.customlization;

import com.care.Constants;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Created by nujian on 2015/5/28.
 */
public class CareContextLoaderListener extends ContextLoaderListener {
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        super.contextDestroyed(event);
        Constants.CURRENT_USER_THREAD_CACHE.remove();
        Constants.RESPONSE_CODE_THREAD_LOCAL.remove();
        Constants.HEADER_SECURITY_TOKEN_THREAD_LOCAL.remove();
    }
}
