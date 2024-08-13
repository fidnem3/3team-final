package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/jobPost")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    @GetMapping("/jobPostCreate")
    public String createJobPost(Model model) {
        model.addAttribute("createJobPostRequestDto", new CreateJobPostRequestDto());
        return "jobPost/jobPostCreate"; // Thymeleaf 템플릿의 경로
    }

    @PostMapping("/jobPostCreate")
    public String createJobPost(@ModelAttribute CreateJobPostRequestDto createJobPostRequestDto) {
        jobPostService.saveJobPost(createJobPostRequestDto.getJobPostId());
        return "redirect:/jobPost/jobPostList";
    }


    // Method to calculate duration (example)
    private long calculateJobPostDuration(Date endDate) {
        // Implement the logic to calculate the duration based on your needs
        long duration = (endDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        return duration;
    }

    @GetMapping("/{id}")
    public String getJobPost(@PathVariable("id") Long jobPostId, Model model) {
        CreateJobPostRequestDto jobPost = jobPostService.getJobPostById(jobPostId);
        model.addAttribute("jobPost", jobPost);
        return "jobPostDetail";
    }

    @GetMapping("/list")
    public String listAllJobPosts(Model model) {
        List<CreateJobPostRequestDto> jobPosts = jobPostService.getAllJobPosts();
        model.addAttribute("jobPosts", jobPosts);
        return "jobPostList";
    }

    @GetMapping("/payment")
    public String showPaymentPage(Model model) {
        return "payment"; // payment.html 템플릿을 반환
    }

    @PostMapping("/payment/complete")
    public RedirectView completePayment(@RequestParam("jobPostId") Long jobPostId) {
        // Finalize payment and save job post
        jobPostService.saveJobPost(jobPostId);
        return new RedirectView("/jobPost/jobPostlist");
    }
}
