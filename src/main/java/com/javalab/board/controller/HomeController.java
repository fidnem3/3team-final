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


}
