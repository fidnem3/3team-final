package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import com.javalab.board.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment")
    public String showPaymentPage() {
        return "jobPost/payment"; // Returns the payment.html view
    }

    @GetMapping("/jobPost/total-payment")
    public List<Map<Long, Integer>> getTotalPaymentsForAllJobPosts() {
        return paymentService.getTotalPaymentsForAllJobPosts();
    }
}

