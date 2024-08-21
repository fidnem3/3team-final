package com.javalab.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "error/accessDenied"; // 이 경로는 src/main/resources/templates/error/accessDenied.html에 해당
    }
}
