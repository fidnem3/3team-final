package com.javalab.board.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		log.info("AuthFailureHandler onAuthenticationFailure");

	    String msg = "이메일과 비밀번호를 확인하세요";
	
	    // exception 관련 메세지 처리
	    if (exception instanceof DisabledException) {
        	msg = "DisabledException account";
        } else if(exception instanceof CredentialsExpiredException) {
        	msg = "CredentialsExpiredException account";
        } else if(exception instanceof BadCredentialsException ) {
        	msg = "이메일과 비밀번호를 확인하세요";
        }

		// URL 인코딩 적용
		String encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString());
		setDefaultFailureUrl("/member/login?error=true&exception=" + encodedMsg);
	
	    super.onAuthenticationFailure(request, response, exception);
	}
}
