package com.javalab.board.controller;

import com.javalab.board.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private JobPostService jobPostService;

    @GetMapping("/admin/approve-job-posts")
    public String approveJobPosts(Model model) {
        model.addAttribute("jobPosts", jobPostService.getAllApprovedJobPosts());
        return "admin-approve-job-posts";
    }

   /* @PostMapping("/admin/approve-job-post")
    public String approveJobPost(@RequestParam("jobPostId") Long jobPostId) {
        jobPostService.updateJobPostStatus(jobPostId, "Approved");
        return "redirect:/admin/approve-job-posts";
    }

    @PostMapping("/admin/reject-job-post")
    public String rejectJobPost(@RequestParam("jobPostId") Long jobPostId) {
        jobPostService.updateJobPostStatus(jobPostId, "Rejected");
        return "redirect:/admin/approve-job-posts";
    }*/
}
