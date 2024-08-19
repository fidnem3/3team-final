package com.javalab.board.service;

import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface JobSeekerService extends UserDetailsService {
    /**
     * 새로운 개인 회원을 등록합니다.
     * - @param jobSeekerVo 등록할 개인 회원 정보 객체
     * - @param userRolesVo 개인 회원의 권한 정보를 포함하는 객체
     */
    void registerJobSeeker(JobSeekerVo jobSeekerVo, UserRolesVo userRolesVo);

    /**
     * 주어진 ID에 해당하는 개인 회원의 상세 정보를 조회합니다.
     * - @param jobSeekerId 조회할 개인 회원의 ID
     * - @return 조회된 개인 회원 정보 객체를 포함하는 Optional 객체
     */
    Optional<JobSeekerVo> getJobSeekerDetails(String jobSeekerId);

    /**
     * 개인 회원 정보를 갱신합니다.
     * - @param jobSeekerVo 갱신할 개인 회원 정보 객체
     */
    JobSeekerVo updateJobSeeker(JobSeekerVo jobSeekerVo);

    /**
     * 주어진 ID에 해당하는 개인 회원 정보를 삭제합니다.
     * - @param jobSeekerId 삭제할 개인 회원의 ID
     */
    void deleteJobSeeker(String jobSeekerId);

    /**
     * 사용자 이름으로 사용자 세부 정보를 로드합니다.
     * - @param username 사용자 이름
     * - @return UserDetails 객체
     */
    @Override
    UserDetails loadUserByUsername(String username);


}
