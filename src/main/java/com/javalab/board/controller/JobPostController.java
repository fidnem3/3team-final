package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
    @ResponseBody
    public Map<String, Object> createJobPost(@ModelAttribute CreateJobPostRequestDto createJobPostRequestDto, @RequestParam("file") MultipartFile file) {
        jobPostService.createJobPost(createJobPostRequestDto);

        // Calculate the cost
        long jobPostDurationInDays = calculateJobPostDuration(createJobPostRequestDto.getEndDate());
        int cost = (int) (jobPostDurationInDays * 300);  // Cost calculation logic

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", "/payment/complete"); // URL for payment page
        response.put("cost", cost);

        return response;
    }


    // Method to calculate duration (example)
    private long calculateJobPostDuration(Date endDate) {
        // Implement the logic to calculate the duration based on your needs
        long duration = (endDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        return duration;
    }

    @GetMapping("/jobPost/{id}")
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
    public ModelAndView paymentPage(@RequestParam("amount") String amount) {
        ModelAndView mav = new ModelAndView("payment");
        mav.addObject("amount", amount);
        return mav;
    }

    @PostMapping("/payment/complete")
    public String completePayment(@RequestParam("amount") String amount) {
        // Handle payment completion logic
        // Typically, you would check if the payment was successful
        // and then proceed to save the job post
        return "redirect:/jobPost/list";
    }
}
