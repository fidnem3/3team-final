package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.dto.SuggestionDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.service.JobSeekerScrapService;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.BoardVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerScrapVo;
import com.javalab.board.vo.JobSeekerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private JobSeekerScrapService jobSeekerScrapService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private JobPostService jobPostService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        List<JobPostVo> top5PopularJobPosts = jobPostService.getTop5PopularJobPosts();
        Map<Long, Boolean> scrapStatusMap = new HashMap<>();
        String jobSeekerId = authentication != null && authentication.getPrincipal() instanceof UserDetails
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : null;

        if (jobSeekerId != null) {
            List<JobSeekerScrapVo> scrapList = jobSeekerScrapService.getScrapList(jobSeekerId);
            scrapStatusMap = scrapList.stream()
                    .collect(Collectors.toMap(JobSeekerScrapVo::getJobPostId, scrap -> true));
        }

        model.addAttribute("scrapStatusMap", scrapStatusMap);
        model.addAttribute("top5PopularJobPosts", top5PopularJobPosts);
        return "index"; // View name
    }

    @GetMapping("/contact")
    public String createSuggestionForm(Model model) {
        model.addAttribute("suggestionDto", new SuggestionDto());
        return "contact";
    }

    @GetMapping("/index")
    public String index(@RequestParam(required = false, defaultValue = "") String keyword, Model model, Authentication authentication) {
        if (!keyword.isEmpty()) {
            return "redirect:/jobPost/jobPostList?keyword=" + keyword;
        }
        List<JobPostVo> top5PopularJobPosts = jobPostService.getTop5PopularJobPosts();
        Map<Long, Boolean> scrapStatusMap = new HashMap<>();
        String jobSeekerId = authentication != null && authentication.getPrincipal() instanceof UserDetails
                ? ((UserDetails) authentication.getPrincipal()).getUsername()
                : null;

        if (jobSeekerId != null) {
            List<JobSeekerScrapVo> scrapList = jobSeekerScrapService.getScrapList(jobSeekerId);
            scrapStatusMap = scrapList.stream()
                    .collect(Collectors.toMap(JobSeekerScrapVo::getJobPostId, scrap -> true));
        }

        model.addAttribute("scrapStatusMap", scrapStatusMap);
        model.addAttribute("top5PopularJobPosts", top5PopularJobPosts);
        return "index"; // View name
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // templates/about.html을 렌더링
    }

}
