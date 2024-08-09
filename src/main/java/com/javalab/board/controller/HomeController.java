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

    @GetMapping("/ex01")
    public String ex01(Model model) {
        BoardVo boardVo = BoardVo.builder()
                .bno(1)
                .title("제목1")
                .content("내용1")
                .memberId("java")
                .regDate(new Date())
                .build();

        model.addAttribute("boardVo", boardVo);
        return "ex/ex01"; // src/main/resources/templates/ex/ex01.html 타임리프 페이지
    }


}
