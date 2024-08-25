package com.javalab.board.service;

import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.repository.JobSeekerScrapMapper;
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
    private JobSeekerScrapMapper jobSeekerScrapMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    @Transactional
    public void registerJobSeeker(JobSeekerVo jobSeekerVo, UserRolesVo userRolesVo) {
        // 개인 회원 정보를 등록합니다.
        jobSeekerMapper.insertJobSeeker(jobSeekerVo);

        // UserRolesVo 객체가 null이거나 필수 필드가 비어있는지 확인
        if (userRolesVo == null) {
            userRolesVo = new UserRolesVo();
        }

        // 필수 필드 설정
        if (userRolesVo.getUserId() == null || userRolesVo.getUserId().isEmpty()) {
            userRolesVo.setUserId(jobSeekerVo.getJobSeekerId());
        }
        if (userRolesVo.getUserType() == null || userRolesVo.getUserType().isEmpty()) {
            userRolesVo.setUserType("jobSeeker");
        }
        if (userRolesVo.getRoleId() == null || userRolesVo.getRoleId().isEmpty()) {
            userRolesVo.setRoleId("ROLE_USER"); // 또는 적절한 기본 역할 ID
        }

        // UserRoles 테이블에 권한 정보를 추가합니다.
        userRolesMapper.insertUserRole(userRolesVo);
    }

    @Override
    public Optional<JobSeekerVo> getJobSeekerDetails(String jobSeekerId) {
        return Optional.ofNullable(jobSeekerMapper.selectJobSeekerById(jobSeekerId));
    }

    @Override
    @Transactional
    public JobSeekerVo updateJobSeeker(JobSeekerVo jobSeekerVo) {
        jobSeekerMapper.updateJobSeeker(jobSeekerVo);
        return jobSeekerMapper.selectJobSeekerById(jobSeekerVo.getJobSeekerId());
    }


    @Override
    @Transactional
    public void deleteJobSeeker(String jobSeekerId) {
        // 1. 먼저 UserRoles 테이블에서 관련 레코드 삭제
        // 여기서 "jobSeeker"는 사용자 유형을, "ROLE_USER"는 기본 역할 ID를 나타냅니다.
        // 실제 사용하는 역할 ID에 맞게 수정해야 합니다.
        userRolesMapper.deleteUserRole(jobSeekerId, "jobSeeker", "ROLE_USER");
        jobSeekerScrapMapper.deleteScrapsByJobSeekerId(jobSeekerId);

        // 2. 그 다음 jobSeeker 테이블에서 레코드 삭제
        jobSeekerMapper.deleteJobSeeker(jobSeekerId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JobSeekerVo jobSeeker = jobSeekerMapper.selectJobSeekerById(username);
        if (jobSeeker == null) {
            // 구직자가 아닌 경우 null을 반환하여 CompanyService에서 처리할 수 있도록 함
            return null;
        }


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
