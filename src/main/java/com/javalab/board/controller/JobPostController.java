package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.service.JobSeekerScrapService;
import com.javalab.board.vo.BoardVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerScrapVo;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpSession;
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

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/jobPost")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;
    @Autowired
    private JobSeekerScrapService jobSeekerScrapService;

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
    public String listJobPosts(Model model, Authentication authentication) {
        List<JobPostVo> jobPosts = jobPostService.getAllJobPosts();

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
        log.info("ScrapStatusMap: {}", scrapStatusMap); // 추가된 로그

        model.addAttribute("jobPosts", jobPosts);
        model.addAttribute("scrapStatusMap", scrapStatusMap);
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

            model.addAttribute("amount", amount);
            model.addAttribute("jobPost", jobPostVo);
            return "jobPost/payment"; // Return the name of the Thymeleaf template
        } else {
            return "error"; // Handle the case where the JobPost is not found
        }
    }

    @GetMapping("/detail/{jobPostId}")
    public String detail(@PathVariable("jobPostId") Long jobPostId, Model model) {
        JobPostVo jobPostVo = jobPostService.findJobPostById(jobPostId);

        if (jobPostVo != null) {
            // 날짜 포맷팅
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedEndDate = jobPostVo.getEndDate() != null
                    ? jobPostVo.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                    : "";
            String formattedCreated = jobPostVo.getCreated() != null
                    ? jobPostVo.getCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
                    : "";

            model.addAttribute("jobPost", jobPostVo); // 모델에 추가
            model.addAttribute("formattedEndDate", formattedEndDate);
            model.addAttribute("formattedCreated", formattedCreated);
            return "jobPost/jobPostDetail"; // 공고 상세 페이지로 이동
        } else {
            // 공고를 찾을 수 없는 경우, 목록 페이지로 리다이렉트
            return "redirect:/jobPost/jobPostList";
        }
    }


}

