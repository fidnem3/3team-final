package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.repository.*;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JobPostServiceImpl implements JobPostService {

    @Autowired
    private JobPostMapper jobPostMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private RequiredSkillMapper requiredSkillMapper;

    @Autowired
    private JobSeekerScrapMapper jobSeekerScrapMapper;

    @Override
    @Transactional
    public Long saveJobPost(JobPostVo jobPostVo) {
        jobPostMapper.insertJobPost(jobPostVo);

        // 스킬 저장
        if (jobPostVo.getSkills() != null && !jobPostVo.getSkills().isEmpty()) {
            for (String skill : jobPostVo.getSkills()) {
                jobPostMapper.insertRequiredSkill(jobPostVo.getJobPostId(), skill);
            }
        }
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
                .skills(dto.getSkills())  // 여기 추가
                .status(dto.getStatus())
                .build();
    }

    @Override
    public JobPostVo getJobPostById(Long jobPostId) {
        JobPostVo jobPostVo = jobPostMapper.getJobPostById(jobPostId);

        if (jobPostVo != null) {
//             필요 기술 목록 조회
            List<String> requiredSkills = requiredSkillMapper.getRequiredSkillsByJobPostId(jobPostId);
            jobPostVo.setSkills(requiredSkills);
        }

        return jobPostVo;
    }

    @Override
    public List<JobPostVo> getAllJobPosts() {
        List<JobPostVo> allJobPosts = jobPostMapper.getAllJobPosts();
        for (JobPostVo jobPost : allJobPosts) {
            CompanyVo companyVo = companyMapper.getCompanyById(jobPost.getCompId());
            if (companyVo != null) {
                jobPost.setLogoPath(companyVo.getLogoPath());
                jobPost.setLogoName(companyVo.getLogoName());
                jobPost.setCompanyName(companyVo.getCompanyName());
            } else {
                // 회사 정보를 찾을 수 없는 경우 로그를 남겨서 확인
                log.warn("Company information not found for JobPost with CompId: " + jobPost.getCompId());
            }
        }
        // 결제 상태가 'after_payment'인 공고만 필터링
        return allJobPosts.stream()
                .filter(jobPost -> "After Payment".equals(jobPost.getPaymentStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRequiredSkillsByJobPostId(Long jobPostId) {
        return jobPostMapper.getRequiredSkillsByJobPostId(jobPostId);
    }


    @Override
    public List<JobPostVo> getJobPostsByCompany() {
        String currentCompanyId = getCurrentCompanyId();
        List<JobPostVo> jobPosts = jobPostMapper.selectJobPostsByCompany(currentCompanyId);

        // 모든 공고에 대해 회사 정보를 설정한다
        for (JobPostVo jobPost : jobPosts) {
            CompanyVo companyVo = companyMapper.getCompanyById(currentCompanyId);
            if (companyVo != null) {
                // 회사의 로고 이름과 경로를 공고에 설정한다
                jobPost.setLogoName(companyVo.getLogoName());
                jobPost.setLogoPath(companyVo.getLogoPath());
            }else {
                // 회사 정보를 찾을 수 없는 경우 로그를 남겨서 확인
                log.warn("Company information not found for JobPost with CompId: " + jobPost.getCompId());
            }
        }
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

    @Override
    @Transactional
    public void updateJobPost(JobPostVo jobPostVo) {
        jobPostMapper.updateJobPost(jobPostVo);
    }

    @Override
    public void deleteJobPost(Long jobPostId) {
        jobPostMapper.deleteJobPost(jobPostId);
    }

    @Transactional
    public void deleteJobPostWithScraps(Long jobPostId) {
        // 자식 레코드 삭제
        jobSeekerScrapMapper.deleteScrapsByJobPostId(jobPostId); // 스크랩 Mapper 메서드 호출

        // 자식 레코드 삭제
        applicationMapper.deleteApplicationById(jobPostId);

        // 부모 레코드 삭제
        jobPostMapper.deleteJobPost(jobPostId); // 공고 Mapper 메서드 호출

    }

    @Override
    public void incrementHitCount(Long jobPostId) {
        jobPostMapper.incrementHitCount(jobPostId);
    }


    @Override
    public List<JobPostVo> getTop5PopularJobPosts() {
        // 상위 5개의 인기 공고를 조회한다
        List<JobPostVo> top5JobPosts = jobPostMapper.selectTop5PopularJobPosts();

        // 모든 공고에 대해 회사 정보를 설정하기 위해 CompanyMapper를 사용한다
        for (JobPostVo jobPost : top5JobPosts) {
            // 공고의 회사 ID를 가져와서 회사 정보를 조회한다
            CompanyVo companyVo = companyMapper.getCompanyById(jobPost.getCompId());
            if (companyVo != null) {
                // 회사의 로고 이름과 경로를 공고에 설정한다
                jobPost.setLogoName(companyVo.getLogoName());
                jobPost.setLogoPath(companyVo.getLogoPath());
            }
        }

        return top5JobPosts;
    }


    @Override
    public List<JobPostVo> getAllJobPostsForAdmin() {
        return jobPostMapper.selectAllJobPostsForAdmin();
    }

    @Override
    public List<JobPostVo> getJobPostsByFilters(String address, String education, String experience) {
        return jobPostMapper.selectJobPostsByFilters(address, education, experience);
    }

    @Override
    public List<JobPostVo> searchJobPosts(String keyword) {
        return jobPostMapper.searchJobPosts(keyword);
    }
    @Override
    public int getTotalJobPostViews() {
        List<JobPostVo> jobPosts = jobPostMapper.findAll();
        // 총 조회수를 계산하는 로직
        return jobPosts.stream().mapToInt(JobPostVo::getHitNo).sum();
    }

    @Override
    public int getTotalJobPosts() {
        return jobPostMapper.getTotalJobPosts(); // 매퍼를 통해 총 공고 수를 가져옵니다.
    }
}

