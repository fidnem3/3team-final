package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.vo.JobPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/jobPost")
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;

    // 채용 공고 작성 폼
    @GetMapping("/create.do")
    public String createJobPostForm() {
        return "createJobPost";
    }

    // 채용 공고 작성 처리
    @PostMapping("/create.do")
    public String createJobPost(@ModelAttribute CreateJobPostRequestDto createJobPostRequestDto) {
        // 채용 공고 생성
        Long jobPostId = jobPostService.createJobPost(createJobPostRequestDto);
        // 결제 페이지로 리다이렉트
        return "redirect:/payment/create?jobPostId=" + jobPostId;
    }

    // 채용 공고 목록 조회
    @GetMapping("/list.do")
    public String listJobPost(Model model) {
        // 채용 공고 목록 조회
        List<JobPostVo> jobPosts = jobPostService.listJobPost();
        model.addAttribute("jobPosts", jobPosts);
        return "jobPostList";
    }
}
