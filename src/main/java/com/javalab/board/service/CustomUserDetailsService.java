package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CompanyMapper companyMapper;
    private final JobSeekerMapper jobSeekerMapper;

    @Autowired
    public CustomUserDetailsService(CompanyMapper companyMapper, JobSeekerMapper jobSeekerMapper) {
        this.companyMapper = companyMapper;
        this.jobSeekerMapper = jobSeekerMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 데이터베이스에서 사용자 정보를 조회하여 사용자 유형을 결정
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company != null) {
            return createUserDetails(company);
        }

        JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
        if (jobSeeker != null) {
            return createUserDetails(jobSeeker);
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }

    private String determineUserType(String username) {
        return username.startsWith("comp") ? "company" : "jobSeeker";
    }

    private UserDetails createUserDetails(CompanyVo company) {
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

    private UserDetails createUserDetails(JobSeekerVo jobSeeker) {
        UserRolesVo userRoles = new UserRolesVo();
        userRoles.setUserId(jobSeeker.getJobSeekerId());
        userRoles.setUserType("jobSeeker");
        userRoles.setRoleId("ROLE_USER");

        return new CustomUserDetails(
                jobSeeker.getJobSeekerId(),
                jobSeeker.getPassword(),
                userRoles
        );
    }
}