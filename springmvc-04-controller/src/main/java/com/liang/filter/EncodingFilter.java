package com.liang.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws SecurityException{

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, SecurityException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
