package com.javalab.board.controller;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.ApplicationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // 지원 내역(list) 조회
    @GetMapping("/list")
    public String getApplicationsByJobSeekerId(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jobSeekerId = userDetails.getUsername();

            List<ApplicationDto> applications = applicationService.getApplicationsByJobSeekerId(jobSeekerId);
            model.addAttribute("applications", applications);
        }
        return "application/list";
    }

    // 지원하기 기능
    @PostMapping("/apply")
    public String applyForJob(@RequestParam("resumeId") int resumeId,
                              @RequestParam("jobPostId") Long jobPostId) {

        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String jobSeekerId = userDetails.getUsername();

            // 서비스 메서드 호출
            applicationService.applyForJob(resumeId, jobPostId, jobSeekerId);

            return "redirect:/applications"; // 지원이 완료된 후 리다이렉트할 페이지
        }

        // 인증 정보가 없는 경우 또는 처리 중 오류가 발생한 경우 처리
        return "redirect:/error"; // 적절한 오류 페이지로 리다이렉트
    }
}
