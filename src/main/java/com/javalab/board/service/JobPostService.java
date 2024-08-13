package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.JobPostVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobPostService {
    void saveTemporaryJobPost(CreateJobPostRequestDto createJobPostRequestDto);
    void saveJobPost(Long jobPostId);
    void createJobPost(CreateJobPostRequestDto createJobPostRequestDto);
    void updateJobPostStatus(Long jobPostId, String status);
    CreateJobPostRequestDto getJobPostById(Long jobPostId);
    List<CreateJobPostRequestDto> getAllJobPosts();
}
