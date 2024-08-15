package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.repository.JobPostMapper;
import com.javalab.board.vo.JobPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostMapper jobPostMapper;

    @Override
    @Transactional
    public Long saveJobPost(JobPostVo jobPostVo) {
        jobPostMapper.insertJobPost(jobPostVo);
        return jobPostVo.getJobPostId(); // 데이터베이스에서 생성된 ID를 반환
    }


    private JobPostVo CreateJobPostRequestDtoToJobPostVo(CreateJobPostRequestDto dto) {
        return JobPostVo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .position(dto.getPosition())
                .salary(dto.getSalary())
                .experience(dto.getExperience())
                .education(dto.getEducation())
                .address(dto.getAddress())
                .endDate(dto.getEndDate())
                .homepage(dto.getHomepage())
                .status(dto.getStatus())
                .build();
    }

    @Override
    public JobPostVo getJobPostById(Long jobPostId) {
        return jobPostMapper.getJobPostById(jobPostId);
    }

    @Override
    public List<JobPostVo> getAllJobPosts() {
        List<JobPostVo> allJobPosts = jobPostMapper.getAllJobPosts();
        // 결제 상태가 'after_payment'인 공고만 필터링
        return allJobPosts.stream()
                .filter(jobPost -> "After Payment".equals(jobPost.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostVo> getJobPostsByCompany() {
        String currentCompanyId = getCurrentCompanyId();
        return jobPostMapper.selectJobPostsByCompany(currentCompanyId);
    }

    private String getCurrentCompanyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 기업회원 ID가 보통 Username으로 설정됨
    }

    @Override
    public void updatePaymentStatus(Long jobPostId, String paymentStatus) {
        JobPostVo jobPostVo = new JobPostVo();
        jobPostVo.setJobPostId(jobPostId);
        jobPostVo.setPaymentStatus(paymentStatus);
        jobPostMapper.updatePaymentStatus(jobPostVo);
    }

    @Override
    public JobPostVo findJobPostById(Long jobPostId) {
        return jobPostMapper.findJobPostById(jobPostId);
    }

    @Override
    public List<JobPostVo> getScrapList(String jobSeekerId) {
        return jobPostMapper.getScrapList(jobSeekerId);
    }
}
