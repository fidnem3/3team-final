package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.*;
import com.javalab.board.vo.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/jobPost")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private JobSeekerScrapService jobSeekerScrapService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private ApplicationService applicationService;

    // 파일 업로드 디렉토리
    private static final String UPLOAD_DIR = "C:/filetest/upload/";
    // 허용된 파일 확장자
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");


    @GetMapping("/jobPostCreate")
    public String createJobPost(Model model) {
        model.addAttribute("createJobPostRequestDto", new CreateJobPostRequestDto());
        return "jobPost/jobPostCreate";
    }

    @PostMapping("/jobPostCreate")
    public String create(
            @ModelAttribute("createJobPostRequestDto") @Valid CreateJobPostRequestDto createJobPostRequestDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "jobPost/jobPostCreate";
        }

        // 사용자 인증 정보 얻기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String compId = ((UserDetails) authentication.getPrincipal()).getUsername();

        // 기업 정보 조회
        CompanyVo companyVo = companyService.getCompanyById(compId);
        String logoPath = companyVo != null ? companyVo.getLogoPath() : null;
        String logoName = companyVo != null ? companyVo.getLogoName() : null;

        // DTO를 VO로 변환
        JobPostVo jobPostVo = JobPostVo.builder()
                .compId(compId)
                .title(createJobPostRequestDto.getTitle())
                .content(createJobPostRequestDto.getContent())
                .position(createJobPostRequestDto.getPosition())
                .salary(createJobPostRequestDto.getSalary())
                .experience(createJobPostRequestDto.getExperience())
                .education(createJobPostRequestDto.getEducation())
                .address(createJobPostRequestDto.getAddress())
                .endDate(createJobPostRequestDto.getEndDate())
                .homepage(createJobPostRequestDto.getHomepage())
                .logoPath(logoPath)   // 기업 로고 경로
                .logoName(logoName)   // 기업 로고 이름
                .status("Before payment")
                .build();

        // JobPost 저장
        Long jobPostId = jobPostService.saveJobPost(jobPostVo);

        // 게시물 목록 페이지로 리다이렉트
        return "redirect:/jobPost/jobPostList";
    }


    @GetMapping("/jobPostList")
    public String listJobPosts(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String experience,
            @RequestParam(required = false, defaultValue = "") String keyword,
            Model model,
            Authentication authentication) {


        List<JobPostVo> jobPosts;

        if (keyword != null && !keyword.isEmpty()) {
            // 검색어가 있을 경우 검색된 공고를 가져옴
            jobPosts = jobPostService.searchJobPosts(keyword);
        } else {
            // 필터가 있는 경우 해당 조건으로 공고를 필터링
            jobPosts = jobPostService.getJobPostsByFilters(address, education, experience);
        }


        String jobSeekerId = authentication != null && authentication.getPrincipal() instanceof UserDetails
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : null;

        Map<Long, Boolean> scrapStatusMap = new HashMap<>();
        if (jobSeekerId != null) {
            List<JobSeekerScrapVo> scrapList = jobSeekerScrapService.getScrapList(jobSeekerId);
            scrapStatusMap = scrapList.stream()
                    .collect(Collectors.toMap(JobSeekerScrapVo::getJobPostId, scrap -> true));
        }

        log.info("JobPosts: {}", jobPosts);
        log.info("ScrapStatusMap: {}", scrapStatusMap);

        model.addAttribute("jobPosts", jobPosts);
        model.addAttribute("keyword", keyword); // 검색어를 템플릿에 전달
        model.addAttribute("scrapStatusMap", scrapStatusMap);
        model.addAttribute("filterAddress", address);
        model.addAttribute("filterEducation", education);
        model.addAttribute("filterExperience", experience);

        return "jobPost/jobPostList";
    }


    @GetMapping("/myJobPostList")
    public String getMyJobPosts(Model model) {
        List<JobPostVo> jobPosts = jobPostService.getJobPostsByCompany();

        model.addAttribute("jobPosts", jobPosts);
        return "jobPost/myJobPostList"; // Thymeleaf 템플릿 이름
    }

    @PostMapping("/completePayment")
    public String completePayment(
            @RequestParam Long jobPostId,
            @RequestParam String paymentStatus,
            @RequestParam String imp_uid,
            @RequestParam String merchant_uid
    ) {
        // 로그에 결제 정보를 출력
        log.info("Received payment notification: jobPostId={}, paymentStatus={}, imp_uid={}, merchant_uid={}", jobPostId, paymentStatus, imp_uid, merchant_uid);

        // 결제 상태 업데이트
        jobPostService.updatePaymentStatus(jobPostId, paymentStatus);

        // 결제 상태 로그 출력
        log.info("Payment status updated to: {}", paymentStatus);

        return "redirect:/jobPost/jobPostList";
    }


    @GetMapping("/payment/{jobPostId}")
    public String showPaymentPage(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
        if (jobPostVo != null) {
            // Ensure you handle the conversion from Date to LocalDate properly
            LocalDate createdDate = jobPostVo.getCreated().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = jobPostVo.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            // Calculate the duration in days
            long durationDays = Duration.between(createdDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
            // Calculate the total amount
            long amount = durationDays * 500;

            // Create and save the payment record
            PaymentVo paymentVo = PaymentVo.builder()
                    .compId(jobPostVo.getCompId()) // Use the company ID from the job post
                    .jobPostId(jobPostVo.getJobPostId())
                    .paymentDate(LocalDate.now()) // Set the current date as the payment date
                    .amount(amount)
                    .build();

            paymentService.savePayment(paymentVo);

            // Fetch company details
            CompanyVo companyVo = companyService.getCompanyById(jobPostVo.getCompId());
            String companyName = (companyVo != null) ? companyVo.getCompanyName() : "Unknown";

            // Add attributes to the model
            model.addAttribute("durationsDays", durationDays);
            model.addAttribute("amount", amount);
            model.addAttribute("jobPost", jobPostVo);
            model.addAttribute("companyName", companyName);
            return "jobPost/payment"; // Return the name of the Thymeleaf template
        } else {
            return "error"; // Handle the case where the JobPost is not found
        }
    }

    @GetMapping("/detail/{jobPostId}")
    public String detail(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.findJobPostById(jobPostId);
        // 조회수 증가
        jobPostService.incrementHitCount(jobPostId);


        if (jobPostVo != null) {
            // 날짜 포맷팅
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedEndDate = jobPostVo.getEndDate() != null
                    ? jobPostVo.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                    : "";
            String formattedCreated = jobPostVo.getCreated() != null
                    ? jobPostVo.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                    : "";

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                String loggedInUsername = userDetails.getUsername();

                // 사용자의 이력서 목록 가져오기
                List<ResumeDto> resumeDtoList = resumeService.findAll(loggedInUsername);
                model.addAttribute("resumes", resumeDtoList);
            }

            model.addAttribute("jobPost", jobPostVo); // 모델에 추가
            model.addAttribute("formattedEndDate", formattedEndDate);
            model.addAttribute("formattedCreated", formattedCreated);
            return "jobPost/jobPostDetail"; // 공고 상세 페이지로 이동
        } else {
            // 공고를 찾을 수 없는 경우, 목록 페이지로 리다이렉트
            return "redirect:/jobPost/jobPostList";
        }
    }

    @GetMapping("/edit/{jobPostId}")
    public String editJobPost(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.getJobPostById(jobPostId);
        if (jobPostVo != null) {
            model.addAttribute("createJobPostRequestDto", jobPostVo); // 모델에 추가
            return "jobPost/jobPostEdit";
        } else {
            return "redirect:/jobPost/jobPostList";
        }
    }


    @PostMapping("/edit")
    public String updateJobPost(@ModelAttribute("createJobPostRequestDto") @Valid CreateJobPostRequestDto createJobPostRequestDto,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("BindingResult has errors: {}", bindingResult.getAllErrors());
            return "index";
        }

        // Get the current company ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String compId = ((UserDetails) authentication.getPrincipal()).getUsername();

        // Create JobPostVo from DTO
        JobPostVo jobPostVo = JobPostVo.builder()
                .compId(compId)
                .title(createJobPostRequestDto.getTitle())
                .content(createJobPostRequestDto.getContent())
                .position(createJobPostRequestDto.getPosition())
                .salary(createJobPostRequestDto.getSalary())
                .experience(createJobPostRequestDto.getExperience())
                .education(createJobPostRequestDto.getEducation())
                .address(createJobPostRequestDto.getAddress())
                .endDate(createJobPostRequestDto.getEndDate())
                .homepage(createJobPostRequestDto.getHomepage())
                .jobPostId(createJobPostRequestDto.getJobPostId())  // Ensure jobPostId is set
                .build();

        jobPostService.updateJobPost(jobPostVo);

        return "redirect:/jobPost/myJobPostList";
    }

    @PostMapping("/delete/{jobPostId}")
    public String deleteJobPost(@PathVariable("jobPostId") Long jobPostId) {
        jobPostService.deleteJobPost(jobPostId);
        return "redirect:/jobPost/myJobPostList";
    }



}






