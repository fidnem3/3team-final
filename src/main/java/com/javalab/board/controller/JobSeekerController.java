package com.javalab.board.controller;

import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jobSeeker")
public class JobSeekerController {

    @Autowired
    private JobSeekerService jobSeekerService;

    /**
     * 구직자의 상세 정보를 보여주는 페이지로 이동합니다.
     * @param model Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return jobSeekerDetail.html 페이지
     */
    @GetMapping("/detail")
    public String getJobSeekerDetail(Model model, Authentication authentication) {
        String jobSeekerId = authentication.getName();
        JobSeekerVo jobSeeker = jobSeekerService.getJobSeekerDetails(jobSeekerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        model.addAttribute("jobSeeker", jobSeeker);
        return "jobSeeker/jobSeekerDetail";
    }

    /**
     * 구직자 정보를 수정할 수 있는 페이지로 이동합니다.
     * @param model Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return jobSeekerUpdate.html 페이지
     */
    @GetMapping("/edit")
    public String editJobSeekerDetail(Model model, Authentication authentication) {
        String jobSeekerId = authentication.getName();
        JobSeekerVo jobSeeker = jobSeekerService.getJobSeekerDetails(jobSeekerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        model.addAttribute("jobSeeker", jobSeeker);
        return "jobSeeker/jobSeekerUpdate";
    }

    /**
     * 구직자 정보를 업데이트하고 상세보기 페이지로 리다이렉트합니다.
     * @param jobSeekerVo 수정된 구직자 정보
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/update")
    public String updateJobSeekerDetail(@ModelAttribute("jobSeeker") JobSeekerVo jobSeekerVo, RedirectAttributes redirectAttributes) {
        JobSeekerVo updatedJobSeeker = jobSeekerService.updateJobSeeker(jobSeekerVo);
        redirectAttributes.addFlashAttribute("jobSeeker", updatedJobSeeker);
        return "redirect:/jobSeeker/detail";
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm() {
        return "jobSeeker/withdrawConfirm";
    }

    @PostMapping("/withdraw")
    public String withdrawJobSeeker(Authentication authentication, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String jobSeekerId = authentication.getName();
        try {
            jobSeekerService.deleteJobSeeker(jobSeekerId);

            // 로그아웃 처리
            new SecurityContextLogoutHandler().logout(request, null, null);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 정상적으로 완료되었습니다.");

            return "redirect:/member/login";  // 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 실패 시 에러 메시지 추가
            redirectAttributes.addFlashAttribute("error", "회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/jobSeeker/detail";
        }
    }






}
