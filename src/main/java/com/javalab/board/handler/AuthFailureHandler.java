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
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final LoginMapper loginMapper;

    private String msg;
    private String errorType;

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

        String username = request.getParameter("username");
        String userType = request.getParameter("userType");

        log.info("Login attempt: username={}, userType={}", username, userType);

        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            // 사용자를 찾지 못했거나, 비밀번호가 일치하지 않는 경우
            setErrorInfo("아이디 또는 비밀번호가 올바르지 않습니다.", "invalid_credentials");
        } else {
            try {
                BlacklistDto blacklistDto = loginMapper.getBlacklistInfo(username, userType);
                log.info("Blacklist info for user {}: {}", username, blacklistDto);

                if (blacklistDto != null && blacklistDto.isBlacklisted()) {
                    setErrorInfo("귀하의 계정은 현재 사용이 제한되었습니다. 관리자에게 문의하세요.", "blacklisted");
                } else if ("jobSeeker".equals(userType)) {
                    handleJobSeekerLogin(username);
                } else if ("company".equals(userType)) {
                    handleCompanyLogin(username);
                } else {
                    setErrorInfo("로그인 중 오류가 발생했습니다. 예외 타입: " + exception.getClass().getSimpleName(), "unknown_error");
                }
            } catch (DataAccessException e) {
                log.error("Database access error", e);
                setErrorInfo("데이터베이스 접근 중 오류가 발생했습니다.", "database_error");
            } catch (Exception e) {
                log.error("Unexpected error in AuthFailureHandler", e);
                setErrorInfo("로그인 처리 중 예기치 않은 오류가 발생했습니다.", "unexpected_error");
            }
        }

        log.info("Login result: errorType={}, message={}", errorType, msg);

        request.getSession().setAttribute("loginErrorMessage", msg);
        request.getSession().setAttribute("loginErrorType", errorType);

        setDefaultFailureUrl("/member/login");
        super.onAuthenticationFailure(request, response, exception);
    }


    private void handleJobSeekerLogin(String username) {
        if (companyMapper.selectCompanyById(username) != null) {
            setErrorInfo("개인 회원 로그인 폼에서는 기업 회원으로 로그인할 수 없습니다.", "wrong_form_company");
        } else {
            setErrorInfo("아이디 또는 비밀번호가 올바르지 않습니다.", "invalid_credentials");
        }
    }

    private void handleCompanyLogin(String username) {
        if (jobSeekerMapper.selectJobSeekerById(username) != null) {
            setErrorInfo("기업 회원 로그인 폼에서는 개인 회원으로 로그인할 수 없습니다.", "wrong_form_jobSeeker");
        } else {
            var company = companyMapper.selectCompanyById(username);
            if (company != null) {
                switch (company.getStatus().toUpperCase()) {
                    case "PENDING":
                        setErrorInfo("계정이 승인 대기 중입니다. 승인을 기다려주세요.", "Pending");
                        break;
                    case "REJECTED":
                        setErrorInfo("계정이 거절되었습니다. 관리자에게 문의하세요.", "Rejected");
                        break;
                    default:
                        setErrorInfo("아이디 또는 비밀번호가 올바르지 않습니다.", "invalid_credentials");
                }
            } else {
                setErrorInfo("아이디 또는 비밀번호가 올바르지 않습니다.", "invalid_credentials");
            }
        }
    }

    private void setErrorInfo(String message, String type) {
        this.msg = message;
        this.errorType = type;
    }
}
