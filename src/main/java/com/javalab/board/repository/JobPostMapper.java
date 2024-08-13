package com.javalab.board.repository;

import com.javalab.board.dto.CreateJobPostRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobPostMapper {
    void insertJobPost(CreateJobPostRequestDto createJobPostRequestDto);
    void updateJobPostStatus(Map<String, Object> params);
    CreateJobPostRequestDto selectJobPostById(Long jobPostId);
    List<CreateJobPostRequestDto> selectAllJobPosts();
}

