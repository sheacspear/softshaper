package ru.zorb.security.token;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр аутентификации по токену
 */
public class RestTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String HEADER_SECURITY_TOKEN = "myresttoken";

	public RestTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		setAuthenticationSuccessHandler(new TokenAuthenticationSuccessHandler());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String token = request.getHeader(HEADER_SECURITY_TOKEN);
		if (token == null) {
			token = request.getParameter(HEADER_SECURITY_TOKEN);
		}
		if (token == null && request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (HEADER_SECURITY_TOKEN.equals(cookie.getName())) {
					token = cookie.getValue();
				}
			}
		}
		if (token == null) {
			TokenAuthentication authentication = new TokenAuthentication(null, null);
			authentication.setAuthenticated(false);
			// return authentication;
			throw new AuthenticationCredentialsNotFoundException("Token not found");
		}
		TokenAuthentication tokenAuthentication = new TokenAuthentication(token, request);
		return getAuthenticationManager().authenticate(tokenAuthentication);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		super.doFilter(req, res, chain);
	}
}
