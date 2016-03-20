package com.care.customlization.filter;

import com.care.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by nujian on 16/2/24.
 */
@Component
public class ClearThreadLocalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Constants.RESPONSE_CODE_THREAD_LOCAL.remove();
        Constants.CURRENT_USER_THREAD_CACHE.remove();
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
