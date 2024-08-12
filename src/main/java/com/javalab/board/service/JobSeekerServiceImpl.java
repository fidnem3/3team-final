package com.javalab.board.service;

import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.repository.UserRolesMapper;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerMapper jobSeekerMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    @Transactional
    public void registerJobSeeker(JobSeekerVo jobSeekerVo, UserRolesVo userRolesVo) {
        // 개인 회원 정보를 등록합니다.
        jobSeekerMapper.insertJobSeeker(jobSeekerVo);

        // UserRoles 테이블에 권한 정보를 추가합니다.
        userRolesMapper.insertUserRole(userRolesVo);
    }

    @Override
    public Optional<JobSeekerVo> getJobSeekerDetails(String jobSeekerId) {
        return Optional.ofNullable(jobSeekerMapper.selectJobSeekerById(jobSeekerId));
    }

    @Override
    @Transactional
    public void updateJobSeeker(JobSeekerVo jobSeekerVo) {
        jobSeekerMapper.updateJobSeeker(jobSeekerVo);
    }

    @Override
    @Transactional
    public void deleteJobSeeker(String jobSeekerId) {
        jobSeekerMapper.deleteJobSeeker(jobSeekerId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 개인 회원 정보 조회 로직
        JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
        if (jobSeeker == null) {
            throw new UsernameNotFoundException("Job Seeker not found with username: " + username);
        }

        // 권한 정보를 조회
        List<GrantedAuthority> authorities = userRolesMapper.selectUserRole(username, "jobSeeker")
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleId()))
                .collect(Collectors.toList());

        return new User(
                jobSeeker.getJobSeekerId(),
                jobSeeker.getPassword(),
                authorities
        );
    }
}
