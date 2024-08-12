package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface JobPostService {
    JobPostVo getJobPostById(Long jobPostId);
   // void updateJobPostStatus(Long jobPostId, String status);
    List<JobPostVo> getAllApprovedJobPosts();
    Long createJobPost(CreateJobPostRequestDto createJobPostRequestDto);
    List<JobPostVo> listJobPost();
}