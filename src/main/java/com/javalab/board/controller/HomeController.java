package com.javalab.board.controller;

import com.javalab.board.service.JobPostService;
import com.javalab.board.service.JobSeekerScrapService;
import com.javalab.board.vo.BoardVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerScrapVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private JobSeekerScrapService jobSeekerScrapService;

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
    public String contact() {
        return "contact"; // templates/contact.html을 렌더링
    }

    @GetMapping("/index")
    public String index(Model model, Authentication authentication) {
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
