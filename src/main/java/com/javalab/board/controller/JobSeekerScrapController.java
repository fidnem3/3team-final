package com.javalab.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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


    @PostMapping("/scrap/toggle")
    public ResponseEntity<Map<String, Object>> toggleScrap(@RequestBody Map<String, Object> request, Authentication authentication) {
        Long jobPostId = Long.valueOf(request.get("jobPostId").toString());
        boolean isScrapped = Boolean.parseBoolean(request.get("isScrapped").toString());

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            // 로그인하지 않은 경우, 로그인 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/login").build();
        }

        // Get the authenticated job seeker ID
        String jobSeekerId = ((UserDetails) authentication.getPrincipal()).getUsername();

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
        String jobSeekerId = ((UserDetails) authentication.getPrincipal()).getUsername();

        List<JobSeekerScrapVo> jobSeekerScrapList = jobSeekerScrapService.getScrapList(jobSeekerId);
        model.addAttribute("scrapList", jobSeekerScrapList);

        return "scrap/jobSeekerScrapList"; // HTML 파일 이름
    }

}
