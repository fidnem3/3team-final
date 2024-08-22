package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;

import java.util.List;
import java.util.Optional;

public interface JobPostService {
    Long saveJobPost(JobPostVo jobPostVoo);
    JobPostVo getJobPostById(Long jobPostId);
    List<JobPostVo> getAllJobPosts();

    List<String> getRequiredSkillsByJobPostId(Long jobPostId);

    List<JobPostVo> getJobPostsByCompany();
    void updatePaymentStatus(Long jobPostId, String paymentStatus);
    JobPostVo findJobPostById(Long jobPostId);
    public List<JobPostVo> getScrapList(String jobSeekerId);
    void updateJobPost(JobPostVo jobPostVo);
    void deleteJobPost(Long jobPostId);
    void incrementHitCount(Long jobPostId);
    List<JobPostVo> getTop5PopularJobPosts();
    List<JobPostVo> getAllJobPostsForAdmin();
    List<JobPostVo> searchJobPosts(String keyword);
    int getTotalJobPostViews(); // 총 공고 조회한 수
    int getTotalJobPosts(); // 총 공고 수를 반환하는 메서드
    List<JobPostVo> getJobPostsByFilters(String address, String education, String experience);
}
