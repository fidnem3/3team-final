package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.vo.AdminVo;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;
    private final AdminService adminService;

    @Autowired
    public CustomUserDetailsService(CompanyMapper companyMapper, JobSeekerMapper jobSeekerMapper, AdminService adminService) {
        this.companyMapper = companyMapper;
        this.jobSeekerMapper = jobSeekerMapper;
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 관리자 계정 조회
        Optional<AdminVo> adminOpt = adminService.getAdminDetails(username);
        if (adminOpt.isPresent()) {
            AdminVo admin = adminOpt.get();
            return createUserDetails(admin);
        }

        // 회사 계정 조회
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company != null) {
            return createUserDetails(company);
        }

        // 구직자 계정 조회
        JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
        if (jobSeeker != null) {
            return createUserDetails(jobSeeker);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    private UserDetails createUserDetails(CompanyVo company) {
        // 회사 상태 확인
        if ("PENDING".equalsIgnoreCase(company.getStatus())) {
            throw new DisabledException("계정이 승인 대기 중입니다. 승인을 기다려주세요.");
        }

        if ("REJECTED".equalsIgnoreCase(company.getStatus())) {
            throw new DisabledException("계정이 거절되었습니다. 관리자에게 문의하세요.");
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
