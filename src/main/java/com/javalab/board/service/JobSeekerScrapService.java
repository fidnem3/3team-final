package com.javalab.board.service;

import com.javalab.board.repository.JobPostMapper;
import com.javalab.board.repository.JobSeekerScrapMapper;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerScrapVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobSeekerScrapService {

    @Autowired
    private JobSeekerScrapMapper scrapMapper;
    @Autowired
    private JobPostMapper jobPostMapper;

    public boolean checkIfScrapped(String jobSeekerId, Long jobPostId) {
        return scrapMapper.existsByJobSeekerIdAndJobPostId(jobSeekerId, jobPostId);
    }

    public void insertScrap(JobSeekerScrapVo scrap) {
        boolean isScrapped = scrapMapper.existsByJobSeekerIdAndJobPostId(scrap.getJobSeekerId(), scrap.getJobPostId());
        if (isScrapped) {
            // 이미 스크랩된 경우 스크랩 삭제
            deleteScrap(scrap.getScrapId());
        } else {
            // 스크랩 추가
            scrapMapper.insertScrap(scrap);
        }
    }

    public void deleteScrap(Long scrapId) {
        scrapMapper.deleteScrap(scrapId);
    }

    public void deleteScrapByJobSeekerIdAndJobPostId(String jobSeekerId, Long jobPostId) {
        scrapMapper.deleteScrapByJobSeekerIdAndJobPostId(jobSeekerId, jobPostId);
    }


    public List<JobSeekerScrapVo> getScrapList(String jobSeekerId) {
        List<JobSeekerScrapVo> scrapList = scrapMapper.getScrapListByJobSeekerId(jobSeekerId);

        for (JobSeekerScrapVo scrap : scrapList) {
            JobPostVo jobPost = jobPostMapper.getJobPostDetailsById(scrap.getJobPostId());
            scrap.setTitle(jobPost.getTitle());
            scrap.setSalary(jobPost.getSalary());
            scrap.setAddress(jobPost.getAddress());
            scrap.setEndDate(jobPost.getEndDate());
            scrap.setLogoName(jobPost.getLogoName()); // JobPost에서 로고 이름을 설정
            scrap.setLogoPath(jobPost.getLogoPath()); // JobPost에서 로고 경로를 설정
        }

        return scrapList;
    }
}