package com.javalab.board.controller;


import com.javalab.board.dto.MonthlyOverviewDto;
import com.javalab.board.service.ApplicationService;
import com.javalab.board.service.CompanyService;
import com.javalab.board.service.JobPostService;
import com.javalab.board.service.PaymentService;
import com.javalab.board.vo.CompanyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JobPostService jobPostService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CompanyService companyService;

    @GetMapping("/all-job-posts")
    public ResponseEntity<?> getTotalPaymentsForAllJobPosts() {
        try {
            List<Map<Long, Integer>> totalPayments = paymentService.getTotalPaymentsForAllJobPosts();
            return ResponseEntity.ok(totalPayments);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving payments for all job posts");
        }
    }
    @GetMapping("/total-job-post-views")
    public int getTotalJobPostViews() {
        return jobPostService.getTotalJobPostViews();
    }

    @GetMapping("/total-job-posts")
    public int getTotalJobPosts() {
        return jobPostService.getTotalJobPosts(); // 총 공고 수를 반환합니다.
    }


    @GetMapping("/total-applications")
    public int getTotalApplications() {
        return applicationService.getTotalApplications();
    }

    @GetMapping("/monthly-overview")
    public List<MonthlyOverviewDto> getMonthlyOverview(@RequestParam("year") int year) {
        return paymentService.getMonthlyOverview(year);
    }


    @GetMapping("/ratio")
    public Map<String, Double> getRatio() {
        int totalJobPosts = jobPostService.getTotalJobPosts();
        int totalApplications = applicationService.getTotalApplications();

        // 전체 수를 기준으로 비율 계산
        double total = totalJobPosts + totalApplications;
        double jobPostPercentage = (total == 0) ? 0 : (totalJobPosts / total) * 100;
        double applicationPercentage = (total == 0) ? 0 : (totalApplications / total) * 100;

        // 비율 정보를 담을 Map 생성
        Map<String, Double> ratioMap = new HashMap<>();
        ratioMap.put("jobPostPercentage", jobPostPercentage);
        ratioMap.put("applicationPercentage", applicationPercentage);

        return ratioMap;
    }


}
