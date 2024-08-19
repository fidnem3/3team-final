package com.javalab.board.controller;

import com.javalab.board.repository.JobSeekerMapper;
import com.javalab.board.service.*;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private BlacklistService blacklistService;


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



    /**
     * 구직자 목록을 보여주는 페이지로 이동합니다.
     */
    @GetMapping("/jobSeekerList")
    public String listJobSeekers(Model model) {
        List<JobSeekerVo> jobSeekers = adminService.getAllJobSeekers();
        model.addAttribute("jobSeekers", jobSeekers);
        return "admin/jobSeekerList";
    }

    @PostMapping("/deleteJobSeeker/{id}")
    public String deleteJobSeeker(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        try {
            jobSeekerService.deleteJobSeeker(id);
            redirectAttributes.addFlashAttribute("message", "구직자가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "구직자 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/jobSeekerList";
    }

    /**
     * 특정 구직자의 상세 정보를 보여주는 페이지로 이동합니다.
     * @param jobSeekerId 조회할 구직자의 ID
     * @param model Spring의 Model 객체
     * @return jobSeekerDetail.html 페이지
     */
    @GetMapping("/jobSeeker/detail/{id}")
    public String getJobSeekerDetailById(@PathVariable("id") String jobSeekerId, Model model, RedirectAttributes redirectAttributes) {
        Optional<JobSeekerVo> jobSeekerOpt = jobSeekerService.getJobSeekerDetails(jobSeekerId);
        if (jobSeekerOpt.isPresent()) {
            model.addAttribute("jobSeeker", jobSeekerOpt.get());
            return "jobSeeker/jobSeekerDetail";
        } else {
            redirectAttributes.addFlashAttribute("error", "해당 구직자를 찾을 수 없습니다.");
            return "redirect:/admin/jobSeekerList";
        }
    }


    /**
     * 기업 목록을 보여주는 페이지로 이동합니다.
     */
    @GetMapping("/companyList")
    public String listCompanies(Model model) {
        List<CompanyVo> companies = adminService.getAllCompanies();
        model.addAttribute("companies", companies);
        return "admin/companyList";
    }

    /**
     * 특정 기업의 상세 정보를 보여주는 페이지로 이동합니다.
     * @param compId 조회할 기업의 ID
     * @param model Spring의 Model 객체
     * @return companyDetail.html 페이지
     */
    @GetMapping("/company/detail/{id}")
    public String getCompanyDetailById(@PathVariable("id") String compId, Model model, RedirectAttributes redirectAttributes) {
        Optional<CompanyVo> companyOpt = companyService.getCompanyDetails(compId);
        if (companyOpt.isPresent()) {
            model.addAttribute("company", companyOpt.get());
            return "company/companyDetail";
        } else {
            redirectAttributes.addFlashAttribute("error", "해당 기업을 찾을 수 없습니다.");
            return "redirect:/admin/companyList";
        }
    }

    /**
     * 기업을 삭제하는 메소드입니다.
     */
    @PostMapping("/deleteCompany/{id}")
    public String deleteCompany(@PathVariable("id") String compId, RedirectAttributes redirectAttributes) {
        try {
            companyService.deleteCompany(compId);
            redirectAttributes.addFlashAttribute("message", "기업이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "기업 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return "redirect:/admin/companyList";
    }



}
