package com.javalab.board.repository;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.JobPostVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JobPostMapper {
    // 반환 타입을 Long으로 설정
    Long insertJobPost(JobPostVo jobPostVo);

    JobPostVo getJobPostById(@Param("jobPostId") Long jobPostId);

    List<JobPostVo> getAllJobPosts();

    List<JobPostVo> selectJobPostsByCompany(String compId);

    void updatePaymentStatus(JobPostVo jobPostVo);
}
