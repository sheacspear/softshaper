package ru.softshaper.services.security.token;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Хендлер успешной авторизации
 */
class TokenAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authentication);
    //Cookie cookie = new Cookie(RestTokenAuthenticationFilter.HEADER_SECURITY_TOKEN, ((TokenAuthentication) authentication).getToken());
    //cookie.setMaxAge(60 * 60 * 24);
    //cookie.setDomain(request.getServerName());
    //cookie.setPath("/" + request.getContextPath());
    //response.addCookie(cookie);
    request.getRequestDispatcher(request.getServletPath() + request.getPathInfo()).forward(request, response);
  }
}
