package com.javalab.board.controller;

import com.javalab.board.vo.BoardVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index"; // src/main/resources/templates/index.html 타임리프 페이지
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact"; // templates/contact.html을 렌더링
    }

    @GetMapping("/index")
    public String index() {
        return "index"; // templates/index.html을 렌더링
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // templates/about.html을 렌더링
    }

}
