package com.javalab.board.service;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobSeekerMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

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
        if (username.startsWith("comp")) {
            CompanyVo company = companyMapper.selectCompanyById(username);
            if (company == null) {
                throw new UsernameNotFoundException("Company not found: " + username);
            }
            return createUserDetails(company);
        } else {
            JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
            if (jobSeeker == null) {
                throw new UsernameNotFoundException("Job Seeker not found: " + username);
            }
            return createUserDetails(jobSeeker);
        }
    }

    private UserDetails createUserDetails(CompanyVo company) {
        return new org.springframework.security.core.userdetails.User(
                company.getCompId(),
                company.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_COMPANY"))
        );
    }

    private UserDetails createUserDetails(JobSeekerVo jobSeeker) {
        return new org.springframework.security.core.userdetails.User(
                jobSeeker.getJobSeekerId(),
                jobSeeker.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}