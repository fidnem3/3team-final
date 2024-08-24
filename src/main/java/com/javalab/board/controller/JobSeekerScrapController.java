package com.javalab.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javalab.board.service.JobPostService;
import com.javalab.board.service.CompanyService; // 추가된 임포트
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User; // 추가된 임포트
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.javalab.board.service.JobSeekerScrapService;
import com.javalab.board.vo.JobSeekerScrapVo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/jobSeeker")
public class JobSeekerScrapController {

    @Autowired
    private JobSeekerScrapService jobSeekerScrapService;

    @Autowired
    private JobPostService jobPostService; // 추가된 임포트
    @Autowired
    private CompanyService companyService; // 추가된 임포트

    @PostMapping("/scrap/toggle")
    public ResponseEntity<Map<String, Object>> toggleScrap(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long jobPostId = Long.valueOf(request.get("jobPostId").toString());
        boolean isScrapped = Boolean.parseBoolean(request.get("isScrapped").toString());

        // Check authentication and determine user ID
        String jobSeekerId = null;
        if (authentication == null) {
            // 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/login").build();
        }

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            jobSeekerId = oauth2User.getName(); // OAuth2User의 getName() 메서드를 사용
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jobSeekerId = userDetails.getUsername(); // UserDetails의 getUsername() 메서드를 사용
        } else {
            // 인증 정보를 확인할 수 없는 경우, 로그인 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/login").build();
        }

        // Create a scrap VO object with required fields
        JobSeekerScrapVo scrapVo = new JobSeekerScrapVo();
        scrapVo.setJobSeekerId(jobSeekerId);
        scrapVo.setJobPostId(jobPostId);

        // Toggle scrap status
        if (isScrapped) {
            jobSeekerScrapService.deleteScrapByJobSeekerIdAndJobPostId(jobSeekerId, jobPostId);
        } else {
            jobSeekerScrapService.insertScrap(scrapVo);
        }

        // Return response indicating the new scrap status
        Map<String, Object> response = new HashMap<>();
        response.put("isScrapped", !isScrapped); // Toggle the state for response
        return ResponseEntity.ok(response);
    }

    @GetMapping("/scrap/list")
    public String listJobSeekerScrap(Authentication authentication, Model model) {
        // Check authentication and determine user ID
        String jobSeekerId = null;
        if (authentication == null) {
            // 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            jobSeekerId = oauth2User.getName(); // OAuth2User의 getName() 메서드를 사용
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            jobSeekerId = userDetails.getUsername(); // UserDetails의 getUsername() 메서드를 사용
        } else {
            // 인증 정보를 확인할 수 없는 경우, 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // Get the list of scrapped job posts
        List<JobSeekerScrapVo> jobSeekerScrapList = jobSeekerScrapService.getScrapList(jobSeekerId);
        model.addAttribute("scrapList", jobSeekerScrapList);

        // Optional: Fetch additional details like company info if needed
        for (JobSeekerScrapVo scrapVo : jobSeekerScrapList) {
            Long jobPostId = scrapVo.getJobPostId();
            JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
            if (jobPostVo != null) {
                String compId = jobPostVo.getCompId();
                CompanyVo companyVo = companyService.getCompanyById(compId);
                if (companyVo != null) {
                    scrapVo.setLogoName(companyVo.getLogoName());
                    scrapVo.setLogoPath(companyVo.getLogoPath());
                    scrapVo.setCompanyName(companyVo.getCompanyName());
                }
            }
        }

        return "scrap/jobSeekerScrapList"; // HTML 파일 이름
    }
}
