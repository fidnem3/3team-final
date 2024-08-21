package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.JobPostMapper;
import com.javalab.board.repository.JobSeekerScrapMapper;
import com.javalab.board.vo.*;
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
    private JobPostMapper jobPostMapper;@Autowired
    private CompanyMapper companyMapper;


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

            if (jobPost != null) {
                CompanyVo companyVo = companyMapper.getCompanyById(jobPost.getCompId());

                // Check if companyVo is null
                if (companyVo != null) {
                    scrap.setLogoName(companyVo.getLogoName());
                } else {
                    // Handle the case where companyVo is null
                    scrap.setLogoName("defaultLogoName"); // Set a default logo name or handle accordingly
                }

                // Set other details
                scrap.setTitle(jobPost.getTitle());
                scrap.setSalary(jobPost.getSalary());
                scrap.setAddress(jobPost.getAddress());
                scrap.setEndDate(jobPost.getEndDate());
            } else {
                // Handle the case where jobPost is null if needed
            }
        }

        return scrapList;
    }

}