package com.javalab.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping
    public String paymentPage() {
        // Show payment page
        return "payment";
    }
}
