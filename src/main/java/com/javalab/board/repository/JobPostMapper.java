package com.javalab.board.repository;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.JobPostVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface JobPostMapper {
    Long insertJobPost(JobPostVo jobPostVo);

    JobPostVo getJobPostById(@Param("jobPostId") Long jobPostId);

    List<JobPostVo> getAllJobPosts();

    JobPostVo getJobPostDetailsById(Long jobPostId);

    List<JobPostVo> selectJobPostsByCompany(String compId);

    void updatePaymentStatus(JobPostVo jobPostVo);

    JobPostVo findJobPostById(Long jobPostId);

    List<JobPostVo> getScrapList(String jobSeekerId);

    void updateJobPost(JobPostVo jobPostVo);

    void deleteJobPost(Long jobPostId);
}
