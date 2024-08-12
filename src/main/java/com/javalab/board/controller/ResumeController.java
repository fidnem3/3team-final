package com.javalab.board.controller;


import com.javalab.board.dto.BoardDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.service.ResumeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
@Slf4j
public class ResumeController {
    private final ResumeService resumeService;

//   저장 폼
    @GetMapping("/save")    public String save() {
        return "/resume/save";

    }
//  저장 폼 전송
    @PostMapping("/save")
    public String save(@ModelAttribute ResumeDto resumeDto){
        System.out.println("resumeDto = " + resumeDto);
        resumeService.save(resumeDto);
        return "index";
    }






}
