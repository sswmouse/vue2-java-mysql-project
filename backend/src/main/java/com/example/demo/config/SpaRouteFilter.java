package com.example.demo.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SPA路由过滤器
 * 将所有非静态资源和非API请求重定向到index.html
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpaRouteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        // 检查是否是静态资源或API请求
        if (isStaticResource(path) || isApiRequest(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 对于SPA路由，重定向到index.html
        // 注意：这里使用forward而不是redirect，以保持URL不变
        request.getRequestDispatcher("/index.html").forward(request, response);
    }

    private boolean isStaticResource(String path) {
        return path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/img/") ||
               path.startsWith("/fonts/") ||
               path.startsWith("/uploads/") ||
               path.equals("/favicon.ico");
    }

    private boolean isApiRequest(String path) {
        return path.startsWith("/api/") ||
               path.startsWith("/auth/") ||
               path.startsWith("/upload/");
    }
}
