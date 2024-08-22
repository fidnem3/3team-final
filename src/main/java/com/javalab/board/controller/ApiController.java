package com.javalab.board.controller;


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
}
