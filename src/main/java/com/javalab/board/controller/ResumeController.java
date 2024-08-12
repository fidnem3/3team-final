package com.javalab.board.controller;


import com.javalab.board.dto.BoardDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.service.ResumeService;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/resume")
@Slf4j
public class ResumeController {

    @Autowired
    private final ResumeService resumeService;

    @Autowired
    private final JobSeekerService jobSeekerService;

    //   저장 폼
    @GetMapping("/save")
    public String save(Model model, HttpSession session) {


        Optional<JobSeekerVo> optionalJobSeekerVo = jobSeekerService.getJobSeekerDetails("koko3"); // 실제 ID로 변경

        if (optionalJobSeekerVo.isPresent()) {
            model.addAttribute("jobSeekerVo", optionalJobSeekerVo.get());
        } else {
            log.error("JobSeekerVo를 찾을 수 없습니다.");
            return "redirect:/error"; // 또는 다른 적절한 처리
        }

        return "/resume/save";
    }


    //  저장 폼 전송
    @PostMapping("/save")
    public String save(@ModelAttribute ResumeDto resumeDto) {
        System.out.println("resumeDto = " + resumeDto);
        resumeService.save(resumeDto);
        return "index";
    }

}
