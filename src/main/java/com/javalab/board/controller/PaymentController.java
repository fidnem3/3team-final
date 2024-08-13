package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.util.Date;

@RequestMapping("/payment")
@Controller
public class PaymentController {

    @Autowired
    private JobPostService jobPostService;

    @PostMapping("/complete")
    @ResponseBody
    public String paymentComplete(@RequestParam("amount") Integer amount,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @RequestParam("position") String position,
                                  @RequestParam("salary") String salary,
                                  @RequestParam("experience") String experience,
                                  @RequestParam("education") String education,
                                  @RequestParam("homepage") String homepage,
                                  @RequestParam("logoPath") String logoPath,
                                  @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        // 공고 등록 정보 생성
        CreateJobPostRequestDto createJobPostRequestDto = new CreateJobPostRequestDto();
        createJobPostRequestDto.setTitle(title);
        createJobPostRequestDto.setContent(content);
        createJobPostRequestDto.setPosition(position);
        createJobPostRequestDto.setSalary(salary);
        createJobPostRequestDto.setExperience(experience);
        createJobPostRequestDto.setEducation(education);
        createJobPostRequestDto.setHomepage(homepage);
        createJobPostRequestDto.setLogoPath(logoPath);
        createJobPostRequestDto.setEndDate(endDate);

        // DB에 공고 등록
        jobPostService.createJobPost(createJobPostRequestDto);

        // Payment 페이지로 리다이렉트
        return "/jobPost/jobPostList"; // 리다이렉트할 URL 반환
    }
}
