package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobPostMapper;
import com.javalab.board.repository.UserRolesMapper;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostMapper jobPostMapper;

    @Override
    public Long createJobPost(CreateJobPostRequestDto jobPostDto) {
        // Insert the job post into the database
        jobPostMapper.insertJobPost(jobPostDto);

        // Return the generated ID
        return jobPostDto.getJobPostId(); // jobPostDto 객체에서 ID를 반환합니다
    }

    @Override
    public List<JobPostVo> listJobPost() {
        return jobPostMapper.getAllApprovedJobPosts();
    }

    @Override
    public JobPostVo getJobPostById(Long jobPostId) {
        return jobPostMapper.getJobPostById(jobPostId);
    }

 /*   @Override
    public void updateJobPostStatus(Long jobPostId, String status) {
        jobPostMapper.updateJobPostStatus(Map.of("jobPostId", jobPostId, "status", status));
    }*/

    @Override
    public List<JobPostVo> getAllApprovedJobPosts() {
        return jobPostMapper.getAllApprovedJobPosts();
    }
}