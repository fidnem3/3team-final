package com.javalab.board.security.handler;

import com.javalab.board.dto.SocialMemberDto;
import com.javalab.board.vo.MemberVo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("----------------------------------------------------------");
        log.info("CustomSocialLoginSuccessHandler onAuthenticationSuccess");
        log.info("Authentication Principal: {}", authentication.getPrincipal());

        // 무조건 index 페이지로 리다이렉트
        response.sendRedirect("/index");
    }
}
