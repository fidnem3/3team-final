package com.javalab.board.controller;

import com.javalab.board.service.JobPostService;
import com.javalab.board.vo.JobPostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JobPostService jobPostService;

    @GetMapping("/adminPage")
    public String getAdminPage(Model model) {
        // 필요한 데이터를 모델에 추가할 수 있습니다.
        return "admin/adminPage"; // 이 경로는 src/main/resources/templates/admin/adminPage.html에 해당
    }

    @GetMapping("/settings")
    public String getSettingsPage(Model model) {
        // 설정 페이지로 이동하는 메서드
        return "admin/settings"; // 이 경로는 src/main/resources/templates/admin/settings.html에 해당
    }



@GetMapping("/jobPostsList")
    public String viewAllJobPosts(Model model) {
        List<JobPostVo> allJobPostsForAdmin = jobPostService.getAllJobPostsForAdmin();
        model.addAttribute("jobPosts", allJobPostsForAdmin);
        return "admin/alljobPosts"; // 뷰 이름 (admin/jobPosts.html)
    }

}
