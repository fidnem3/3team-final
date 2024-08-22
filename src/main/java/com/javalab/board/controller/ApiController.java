package com.javalab.board.controller;


import com.javalab.board.dto.YearlyOverviewDto;
import com.javalab.board.service.ApplicationService;
import com.javalab.board.service.JobPostService;
import com.javalab.board.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/yearly-overview")
    public List<YearlyOverviewDto> getYearlyOverview() {
        return applicationService.getYearlyOverview();
    }
}
