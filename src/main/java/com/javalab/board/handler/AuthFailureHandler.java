package com.javalab.board.handler;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.repository.LoginMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final LoginMapper loginMapper;

    @Autowired
    public AuthFailureHandler(CompanyMapper companyMapper, JobSeekerMapper jobSeekerMapper, LoginMapper loginMapper) {
        this.companyMapper = companyMapper;
        this.jobSeekerMapper = jobSeekerMapper;
        this.loginMapper = loginMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("AuthFailureHandler onAuthenticationFailure");
        log.error("Authentication failed: ", exception);

        String msg;
        String errorType;

        String username = request.getParameter("username");
        String userType = request.getParameter("userType");

        log.info("Attempting login for user: " + username + " with type: " + userType);

        try {
            // 블랙리스트 체크
            BlacklistDto blacklistDto = loginMapper.getBlacklistInfo(username, userType);
            log.info("Blacklist info for user " + username + ": " + blacklistDto);

            if (blacklistDto != null && blacklistDto.isBlacklisted()) {
                log.info("User " + username + " is blacklisted");
                msg = "귀하의 계정은 현재 사용이 제한되었습니다. 관리자에게 문의하세요.";
                errorType = "blacklisted";
            } else if ("jobSeeker".equals(userType)) {
                if (companyMapper.selectCompanyById(username) != null) {
                    msg = "개인 회원 로그인 폼에서는 기업 회원으로 로그인할 수 없습니다.";
                    errorType = "wrong_form_company";
                } else {
                    msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
                    errorType = "invalid_credentials";
                }
            } else if ("company".equals(userType)) {
                if (jobSeekerMapper.selectJobSeekerById(username) != null) {
                    msg = "기업 회원 로그인 폼에서는 개인 회원으로 로그인할 수 없습니다.";
                    errorType = "wrong_form_jobSeeker";
                } else {
                    var company = companyMapper.selectCompanyById(username);
                    if (company != null) {
                        switch (company.getStatus()) {
                            case "PENDING":
                                msg = "계정이 승인 대기 중입니다. 승인을 기다려주세요.";
                                errorType = "pending";
                                break;
                            case "REJECTED":
                                msg = "계정이 거절되었습니다. 관리자에게 문의하세요.";
                                errorType = "rejected";
                                break;
                            default:
                                msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
                                errorType = "invalid_credentials";
                        }
                    } else {
                        msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
                        errorType = "invalid_credentials";
                    }
                }
            } else {
                msg = "로그인 중 오류가 발생했습니다. 예외 타입: " + exception.getClass().getSimpleName();
                errorType = "unknown_error";
            }
        } catch (Exception e) {
            log.error("Unexpected error in AuthFailureHandler", e);
            msg = "로그인 처리 중 예기치 않은 오류가 발생했습니다.";
            errorType = "unexpected_error";
        }

        log.info("Login error message: " + msg);
        log.info("Login error type: " + errorType);

        request.getSession().setAttribute("loginErrorMessage", msg);
        request.getSession().setAttribute("loginErrorType", errorType);

        setDefaultFailureUrl("/member/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
