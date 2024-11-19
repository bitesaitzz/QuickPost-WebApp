package com.bitesaitzz.QuickPost.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public class SessionCheckFilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentSessionId = request.getSession().getId();
            String storedSessionId = request.getRequestedSessionId();

            // Если сессии не совпадают, перенаправляем на страницу логина
            if (storedSessionId == null || !storedSessionId.equals(currentSessionId)) {
                SecurityContextHolder.clearContext(); // Очищаем контекст безопасности
                response.sendRedirect("/auth/login?error=sessionExpired"); // Перенаправляем на страницу логина
                return;
            }
        }
        filterChain.doFilter(request, response); // Продолжаем цепочку фильтров
    }


}
