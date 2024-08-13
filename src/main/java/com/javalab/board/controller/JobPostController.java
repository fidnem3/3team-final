package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public String createJobPost(@ModelAttribute CreateJobPostRequestDto createJobPostRequestDto, @RequestParam("file") MultipartFile file) {
        jobPostService.createJobPost(createJobPostRequestDto, file);
        // Redirect to payment page
        return "redirect:/payment";
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
}
