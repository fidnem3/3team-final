package com.javalab.board.service;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.repository.LoginMapper;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.vo.AdminVo;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final AdminService adminService;
    private final LoginMapper loginMapper;

    @Autowired
    public CustomUserDetailsService(CompanyMapper companyMapper, JobSeekerMapper jobSeekerMapper, AdminService adminService, LoginMapper loginMapper) {
        this.companyMapper = companyMapper;
        this.jobSeekerMapper = jobSeekerMapper;
        this.adminService = adminService;
        this.loginMapper = loginMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 관리자 계정 조회
        Optional<AdminVo> adminOpt = adminService.getAdminDetails(username);
        if (adminOpt.isPresent()) {
            AdminVo admin = adminOpt.get();
            log.info("관리자:" + admin);
            return createUserDetails(admin);
        }

        // 회사 계정 조회
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company != null) {
            if (isBlacklisted(company.getCompId(), "company")) {
                log.warn("Blacklisted company account: {}", username);
                return null;
            }
            log.info("회사:" + company);
            return createUserDetails(company);
        }

        // 구직자 계정 조회
        JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
        if (jobSeeker != null) {
            if (isBlacklisted(jobSeeker.getJobSeekerId(), "jobSeeker")) {
                log.warn("Blacklisted job seeker account: {}", username);
                return null;
            }
            log.info("구직자:" + jobSeeker);
            return createUserDetails(jobSeeker);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    private boolean isBlacklisted(String username, String userType) {
        BlacklistDto blacklistDto = loginMapper.getBlacklistInfo(username, userType);
        return blacklistDto != null && blacklistDto.isBlacklisted();
    }

    private UserDetails createUserDetails(CompanyVo company) {
        // 회사 상태 확인
        if ("PENDING".equalsIgnoreCase(company.getStatus()) || "REJECTED".equalsIgnoreCase(company.getStatus())) {
            log.warn("Company account status: {}", company.getStatus());
            return null;
        }

        UserRolesVo userRoles = new UserRolesVo();
        userRoles.setUserId(company.getCompId());
        userRoles.setUserType("company");
        userRoles.setRoleId("ROLE_COMPANY");

        return new CustomUserDetails(
                company.getCompId(),
                company.getPassword(),
                userRoles
        );
    }

    private UserDetails createUserDetails(AdminVo admin) {
        UserRolesVo userRoles = new UserRolesVo();
        userRoles.setUserId(admin.getAdminId());
        userRoles.setUserType("admin");
        userRoles.setRoleId("ROLE_ADMIN");

        return new CustomUserDetails(
                admin.getAdminId(),
                admin.getPassword(),
                userRoles
        );
    }

    private UserDetails createUserDetails(JobSeekerVo jobSeeker) {
        UserRolesVo userRoles = new UserRolesVo();
        userRoles.setUserId(jobSeeker.getJobSeekerId());
        userRoles.setUserType("user");
        userRoles.setRoleId("ROLE_USER");

        return new CustomUserDetails(
                jobSeeker.getJobSeekerId(),
                jobSeeker.getPassword(),
                userRoles
        );
    }
}