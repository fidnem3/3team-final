package com.javalab.board.controller;


import com.javalab.board.dto.BoardDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.dto.ResumeSkillDto;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.service.ResumeService;
import com.javalab.board.vo.BoardVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.MemberVo;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
    public String save(@ModelAttribute("resumeDto") ResumeDto resumeDto ,MultipartFile file) throws Exception{

        System.out.println("resumeDto = " + resumeDto);

        resumeService.resumeCreate(resumeDto , file);
        return "index";
    }


    //이력서 목록 보기
    @GetMapping("/list")
    public String findAll(Model model) {
        List<ResumeDto> resumeDtoList = resumeService.findAll();
        model.addAttribute("resumeList", resumeDtoList);
        return "/resume/list";

    }

    //이력서 상세 보기
    @GetMapping("/detail/{resumeId}")
    public String getResumeDetail(@PathVariable("resumeId") int resumeId, Model model) {

//        조회수 처리
//        resumeService.updateHits(resumeId);

        //상세내용 가져옴
        ResumeDto resumeDto = resumeService.findById(resumeId);

        model.addAttribute("resume", resumeDto);

        return "resume/detail";
    }

//    @GetMapping("/detail/{resumeId}")
//    public String getResumeDetail(@PathVariable("resumeId") int resumeId, Model model) {
//
//        ResumeDto resumeDto = resumeService.findById(resumeId);
//
//        if (resumeDto == null) {
//            // 예외 처리 또는 기본 객체 생성
//            resumeDto = new ResumeDto();  // 기본값을 사용하거나 오류 페이지로 리다이렉트
//        }
//        model.addAttribute("resume", resumeDto);
//        return "resume/detail";
//    }


    /**
     * 게시물 수정폼(화면-Get)
     */
    @GetMapping("/update/{resumeId}")
    public String updateResume(@PathVariable("resumeId") int resumeId,
                               HttpSession session, Model model) {

        ResumeDto resumeDto = resumeService.findById(resumeId);
        model.addAttribute("resume", resumeDto);    // 화면에 보여줄 게시물을 model에 저장
        return "resume/update";
    }

//    /**
//     * 게시물 수정 메소드(Post)
//     */
//    @PostMapping("/update/{resumeId}")
//    public String updateResume(@ModelAttribute ResumeDto resumeDto, Model model,
//                               HttpSession session) {
//
//        resumeService.updateResume(resumeDto);
//        ResumeDto dto = resumeService.findById(resumeDto.getResumeId());
//
//        model.addAttribute("resume" , dto);
//
////         세션에서 조회한 사용자를 작성자로 설정
////        resumeDto.setJobSeekerId(jobSeekerVo.getJobSeekerId());
//
////        resumeService.updateResume(resumeDto);
//
//        return "redirect:/resume/list";	// 목록 요청(listBoard() 호출)
//    }

    @PostMapping("/update/{resumeId}")
    public String update(ResumeDto resumeDto, Model model) {
        resumeService.updateResume(resumeDto);
        ResumeDto dto = resumeService.findById(resumeDto.getResumeId());
        model.addAttribute("resume", dto);
        return "redirect:/list";

    }

//    @GetMapping("/delete/{resumeId}")
//    public String deleteResume(@PathVariable("resumeId") int resumeId) {
//        resumeService.deleteResume(resumeId);
//        return
//                "redirect:/list";    // 목록 요청(listBoard() 호출)
//    }

    @PostMapping("/delete/{resumeId}")
    public String deleteResume(@PathVariable("resumeId") int resumeId) {
        resumeService.deleteResume(resumeId);
        return "redirect:/resume/list";  // 삭제 후 목록 페이지로 리다이렉션
    }
}




