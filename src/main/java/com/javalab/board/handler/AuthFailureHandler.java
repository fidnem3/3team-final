package com.javalab.board.handler;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;

    @Autowired
    public AuthFailureHandler(CompanyMapper companyMapper, JobSeekerMapper jobSeekerMapper) {
        this.companyMapper = companyMapper;
        this.jobSeekerMapper = jobSeekerMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("AuthFailureHandler onAuthenticationFailure");
        log.error("Authentication failed: ", exception);  // 예외 로깅 추가


        String msg;
        String errorType;

        String username = request.getParameter("username");
        String userType = request.getParameter("userType");



        if ("jobSeeker".equals(userType) && companyMapper.selectCompanyById(username) != null) {
            msg = "개인 회원 로그인 폼에서는 기업 회원으로 로그인할 수 없습니다.";
            errorType = "wrong_form_company";
        } else if ("company".equals(userType) && jobSeekerMapper.selectJobSeekerById(username) != null) {
            msg = "기업 회원 로그인 폼에서는 개인 회원으로 로그인할 수 없습니다.";
            errorType = "wrong_form_jobSeeker";
        } else if (exception instanceof BadCredentialsException) {
            msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
            errorType = "invalid_credentials";
        } else {
            msg = "로그인 중 오류가 발생했습니다. 예외 타입: " + exception.getClass().getSimpleName();
            errorType = "unknown_error";
        }

        request.getSession().setAttribute("loginErrorMessage", msg);
        request.getSession().setAttribute("loginErrorType", errorType);

        setDefaultFailureUrl("/member/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
