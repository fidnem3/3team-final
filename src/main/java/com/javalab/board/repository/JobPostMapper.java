package com.javalab.board.repository;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.JobPostVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobPostMapper {
    void insertJobPost(CreateJobPostRequestDto jobPostVo);

    JobPostVo getJobPostById(Long id);

    void updateJobPostStatus(Long jobPostId, String status);

    List<JobPostVo> listJobPosts();

    List<JobPostVo> findAllApproved();

    List<JobPostVo> getAllApprovedJobPosts();
}
