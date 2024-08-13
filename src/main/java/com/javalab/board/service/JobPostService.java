package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobPostService {
    void createJobPost(CreateJobPostRequestDto createJobPostRequestDto, MultipartFile file);
    void updateJobPostStatus(Long jobPostId, String status);
    CreateJobPostRequestDto getJobPostById(Long jobPostId);
    List<CreateJobPostRequestDto> getAllJobPosts();
}
