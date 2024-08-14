package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.JobPostVo;

import java.util.List;
import java.util.Optional;

public interface JobPostService {
    Long saveJobPost(JobPostVo jobPostVoo);
    JobPostVo getJobPostById(Long jobPostId);
    List<JobPostVo> getAllJobPosts();
    List<JobPostVo> getJobPostsByCompany();
    void updatePaymentStatus(Long jobPostId, String paymentStatus);
    JobPostVo findJobPostById(Long jobPostId);
}
