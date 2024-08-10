package com.javalab.board.service;

import com.javalab.board.vo.JobSeekerVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



public interface JobSeekerService {
    /**
     * 새로운 개인 회원을 등록합니다.
     * - @param jobSeekerVo 등록할 개인 회원 정보 객체
     */
    void registerJobSeeker(JobSeekerVo jobSeekerVo);

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
    void updateJobSeeker(JobSeekerVo jobSeekerVo);

    /**
     * 주어진 ID에 해당하는 개인 회원 정보를 삭제합니다.
     * - @param jobSeekerId 삭제할 개인 회원의 ID
     */
    void deleteJobSeeker(String jobSeekerId);
}
