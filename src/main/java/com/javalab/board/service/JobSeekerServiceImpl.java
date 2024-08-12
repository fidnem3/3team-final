package com.javalab.board.service;

import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.vo.JobSeekerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    @Autowired
    private JobSeekerMapper jobSeekerMapper;

    /**
     * 새로운 개인 회원을 등록합니다.
     * - @param jobSeekerVo 등록할 개인 회원 정보 객체
     */
    @Override
    public void registerJobSeeker(JobSeekerVo jobSeekerVo) {
        jobSeekerMapper.insertJobSeeker(jobSeekerVo);
    }

    /**
     * 주어진 ID에 해당하는 개인 회원의 상세 정보를 조회합니다.
     * - @param jobSeekerId 조회할 개인 회원의 ID
     * - @return 조회된 개인 회원 정보 객체를 포함하는 Optional 객체
     */
    @Override
    public Optional<JobSeekerVo> getJobSeekerDetails(String jobSeekerId) {
        return Optional.ofNullable(jobSeekerMapper.selectJobSeekerById(jobSeekerId));
    }

    /**
     * 개인 회원 정보를 갱신합니다.
     * - @param jobSeekerVo 갱신할 개인 회원 정보 객체
     */
    @Override
    public void updateJobSeeker(JobSeekerVo jobSeekerVo) {
        jobSeekerMapper.updateJobSeeker(jobSeekerVo);
    }

    /**
     * 주어진 ID에 해당하는 개인 회원 정보를 삭제합니다.
     * - @param jobSeekerId 삭제할 개인 회원의 ID
     */
    @Override
    public void deleteJobSeeker(String jobSeekerId) {
        jobSeekerMapper.deleteJobSeeker(jobSeekerId);
    }
}
