package com.javalab.board.handler;

import com.javalab.board.repository.LoginMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 로그인 성공 시 다음 처리를 담당하는 클래스
 *  - 예를 들면 메인 페이지로 이동할 수도 있다.
 */
@RequiredArgsConstructor
@Component
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginMapper loginMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 모든 사용자에 대해 리다이렉트할 URL 설정
        String redirectUrl = "/index";

        setDefaultTargetUrl(redirectUrl); // 설정한 URL로 리다이렉트

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
