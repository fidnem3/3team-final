package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.vo.JobPostVo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/jobPost")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @GetMapping("/jobPostCreate")
    public String createJobPost(Model model) {
        model.addAttribute("createJobPostRequestDto", new CreateJobPostRequestDto());
        return "jobPost/jobPostCreate";
    }

    @PostMapping("/jobPostCreate")
    public String create(@ModelAttribute("createJobPostRequestDto") @Valid CreateJobPostRequestDto createJobPostRequestDto,
                         BindingResult bindingResult) {
        log.info("CreateJobPostRequestDto: {}", createJobPostRequestDto);

        // 사용자 인증 정보 얻기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String compId = ((UserDetails) authentication.getPrincipal()).getUsername();

        // DTO를 VO로 변환
        JobPostVo jobPostVo = JobPostVo.builder()
                .compId(compId)  // 현재 사용자 ID 설정
                .title(createJobPostRequestDto.getTitle())
                .content(createJobPostRequestDto.getContent())
                .position(createJobPostRequestDto.getPosition())
                .salary(createJobPostRequestDto.getSalary())
                .experience(createJobPostRequestDto.getExperience())
                .education(createJobPostRequestDto.getEducation())
                .address(createJobPostRequestDto.getAddress())
                .endDate(createJobPostRequestDto.getEndDate())
                .homepage(createJobPostRequestDto.getHomepage())
                .status("Before payment") // 기본 상태를 'Pending'으로 설정
                .build();

        // JobPost 저장
        Long jobPostId = jobPostService.saveJobPost(jobPostVo);

        log.info("JobPost created with ID: {}", jobPostId);

        // 게시물 목록 페이지로 리다이렉트
        return "redirect:/jobPost/jobPostList";
    }


    @GetMapping("/jobPostList")
    public String listJobPosts(Model model) {
        List<JobPostVo> jobPosts = jobPostService.getAllJobPosts();
        log.info("JobPosts: {}", jobPosts); // 로그에 공고 목록 출력
        model.addAttribute("jobPosts", jobPosts);
        return "jobPost/jobPostList";
    }

    @GetMapping("/myJobPostList")
    public String getMyJobPosts(Model model) {
        List<JobPostVo> jobPosts = jobPostService.getJobPostsByCompany();
        model.addAttribute("jobPosts", jobPosts);
        return "jobPost/myJobPostList"; // Thymeleaf 템플릿 이름
    }

    @GetMapping("/payment/{jobPostId}")
    public String showPaymentPage(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
        model.addAttribute("jobPostVo", jobPostVo);
        return "jobPost/payment";
    }
}

