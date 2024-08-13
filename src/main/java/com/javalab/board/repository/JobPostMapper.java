package com.javalab.board.repository;

import com.javalab.board.vo.JobPostVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobPostMapper {

    void save(JobPostVo jobPost);

    void updateStatus(@Param("jobPostId") Long jobPostId, @Param("status") String status);

    JobPostVo findById(Long jobPostId);

    List<JobPostVo> findAll();
}
