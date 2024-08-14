package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.vo.JobPostVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
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

  /*  @GetMapping("/payment/{jobPostId}")
    public String showPaymentPage(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
        model.addAttribute("jobPostVo", jobPostVo);
        return "jobPost/payment";
    }*/

    @PostMapping("/completePayment")
    public String completePayment(HttpServletRequest request, Principal principal) {
        String jobPostId = request.getParameter("jobPostId");
        String paymentStatus = "After Payment"; // 결제 완료 상태

        // 현재 사용자 정보를 가져옵니다.
        String currentUserId = principal.getName(); // 사용자 ID 가져오기

        // 공고 소유자 검증 로직
        // DTO나 VO를 사용하여 공고 정보를 가져옵니다
        JobPostVo jobPost = jobPostService.getJobPostById(Long.parseLong(jobPostId));
        if (jobPost == null || !jobPost.getCompId().equals(currentUserId)) {
            // 공고가 존재하지 않거나 소유자가 아닌 경우 권한 거부
            log.error("Unauthorized access attempt by user: {} for jobPostId: {}", currentUserId, jobPostId);
            throw new AccessDeniedException("You do not have permission to complete payment for this job post.");
        }

        // 로그에 요청 파라미터 출력
        log.info("Received payment request with jobPostId: {}, paymentStatus: {}", jobPostId, paymentStatus);

        // 결제 상태 업데이트
        jobPostService.updatePaymentStatus(Long.parseLong(jobPostId), paymentStatus);

        // 결제 상태 로그 출력
        log.info("Payment status updated to: {}", paymentStatus);

        return "redirect:/jobPost/jobPostList";
    }




    @GetMapping("/payment/{jobPostId}")
    public String showPaymentPage(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
        if (jobPostVo != null) {
            LocalDate createdDate = jobPostVo.getCreated().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = jobPostVo.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            // 공고 기간 계산
            long durationDays = Duration.between(createdDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
            // 총 금액 계산 (하루에 500원)
            long amount = durationDays * 500;

            // 디버깅 로그 추가
            System.out.println("Calculated amount: " + amount);

            // 모델에 값 추가
            model.addAttribute("amount", amount);
            model.addAttribute("jobPost", jobPostVo);
            return "jobPost/payment"; // Thymeleaf 템플릿 이름
        } else {
            return "error"; // 공고가 없을 경우 처리
        }
    }


}

