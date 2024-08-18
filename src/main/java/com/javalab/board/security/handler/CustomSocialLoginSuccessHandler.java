package com.javalab.board.security.handler;

import com.javalab.board.dto.SocialMemberDto;
import com.javalab.board.vo.MemberVo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------------------------");
        log.info("CustomSocialLoginSuccessHandler onAuthenticationSuccess");
        log.info("Authentication Principal: {}", authentication.getPrincipal());

        Object principal = authentication.getPrincipal();

        // Handle MemberVo type
        if (principal instanceof MemberVo) {
            MemberVo memberVo = (MemberVo) principal;

            log.info("MemberVo isSocial: {}", memberVo.isSocial());
            boolean passwordMatches = passwordEncoder.matches("1111", memberVo.getPassword());
            log.info("Password matches 1111: {}", passwordMatches);

            if (memberVo.isSocial() && passwordMatches) {
                log.info("Redirecting to password modification page.");
                response.sendRedirect("/member/modify");
            } else {
                log.info("Redirecting to index page.");
                response.sendRedirect("/index");
            }
        }
        // Handle OAuth2User type
        else if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            if (oauth2User instanceof SocialMemberDto) {
                SocialMemberDto socialMemberDto = (SocialMemberDto) oauth2User;

                log.info("SocialMemberDto isSocial: {}", socialMemberDto.isSocial());

                if (socialMemberDto.isSocial()) {
                    log.info("Redirecting to password modification page.");
                    response.sendRedirect("/member/modify");
                } else {
                    log.info("Redirecting to index page.");
                    response.sendRedirect("/index");
                }
            } else {
                log.error("OAuth2User is not an instance of SocialMemberDto: {}", oauth2User.getClass());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected OAuth2User type");
            }
        }
        // Handle unknown principal type
        else {
            log.error("Unknown principal type: {}", principal.getClass());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected principal type");
        }
    }
}
