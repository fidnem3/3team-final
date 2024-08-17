package com.javalab.board.controller;


import com.javalab.board.dto.ResumeDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.service.ResumeService;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/save")
    public String save(Model model) {

        // SecurityContextHolder를 통해 현재 인증된 사용자의 Authentication 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Principal의 실제 클래스를 출력하여 확인
            Object principal = authentication.getPrincipal();

            // CustomUserDetails로 캐스팅 가능한지 확인
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;

                // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
                String loggedInUsername = userDetails.getUsername();

                // jobSeekerId를 사용하여 JobSeekerVo를 조회
                Optional<JobSeekerVo> optionalJobSeekerVo = jobSeekerService.getJobSeekerDetails(loggedInUsername);

                if (optionalJobSeekerVo.isPresent()) {
                    model.addAttribute("jobSeekerVo", optionalJobSeekerVo.get());
                } else {
                    log.error("JobSeekerVo를 찾을 수 없습니다.");
                    return "redirect:/error"; // 또는 다른 적절한 처리
                }

                return "/resume/save"; // 이력서 저장 폼 페이지로 이동
            } else {
                log.error("Authentication principal is not an instance of CustomUserDetails.");
            }
        } else {
            log.error("Authentication is null.");
        }

        // 오류 발생 시 리다이렉트
        return "/resume/save";
    }




    //  저장 폼 전송

    @PostMapping("/save")
    public String save(@ModelAttribute("resumeDto") ResumeDto resumeDto ,@RequestParam("file") MultipartFile file , @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{

        // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
        String loggedInUsername = userDetails.getUsername();
        log.debug("Logged in Username: " + loggedInUsername);


        // resumeDto에 jobSeekerId 설정
        resumeDto.setJobSeekerId(loggedInUsername);
        log.debug("JobSeekerId 설정됨: " + resumeDto.getJobSeekerId());

        if (resumeDto.getJobSeekerId() == null) {
            log.error("JobSeekerId가 null입니다. 저장할 수 없습니다.");
            return "redirect:/error"; // 또는 적절한 에러 처리
        }

        // resumeService를 통해 이력서 저장 및 파일 처리
        resumeService.resumeCreate(resumeDto, file);

        return "redirect:/resume/list";
    }


    // 이력서 목록 보기
    @GetMapping("/list")
    public String findAll(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Principal의 실제 클래스를 출력하여 확인
            Object principal = authentication.getPrincipal();

            // CustomUserDetails로 캐스팅 가능한지 확인
            if (principal instanceof CustomUserDetails) {

                CustomUserDetails userDetails = (CustomUserDetails) principal;

                // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
                String loggedInUsername = userDetails.getUsername();

                // jobSeekerId를 사용하여 이력서 목록 조회
                List<ResumeDto> resumeDtoList = resumeService.findAll(loggedInUsername);

                model.addAttribute("resumeList", resumeDtoList);
                return "/resume/list";
            }
        }
        return "redirect:/login"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
    }


//    //이력서 목록 보기
//    @GetMapping("/list")
//    public String findAll(Model model) {
//        List<ResumeDto> resumeDtoList = resumeService.findAll();
//        model.addAttribute("resumeList", resumeDtoList);
//        return "/resume/list";
//
//    }

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


     /*// 게시물 수정폼(화면-Get)
    @GetMapping("/update/{resumeId}")
    public String updateResume(@PathVariable("resumeId") int resumeId,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
        String loggedInUsername = userDetails.getUsername();

        // jobSeekerId를 사용하여 JobSeekerVo를 조회
        Optional<JobSeekerVo> optionalJobSeekerVo = jobSeekerService.getJobSeekerDetails(loggedInUsername);

        if (optionalJobSeekerVo.isPresent()) {
            model.addAttribute("jobSeekerVo", optionalJobSeekerVo.get());
        } else {
            log.error("JobSeekerVo를 찾을 수 없습니다.");
            return "redirect:/error"; // 또는 다른 적절한 처리
        }

        ResumeDto resumeDto = resumeService.findById(resumeId);

        // 만약 해당 사용자가 작성한 이력서가 아니면 접근을 막는 로직을 추가할 수 있습니다.
        if (!resumeDto.getJobSeekerId().equals(loggedInUsername)) {
            log.error("사용자가 작성한 이력서가 아닙니다.");
            return "redirect:/error"; // 또는 다른 적절한 처리
        }

        model.addAttribute("resume", resumeDto);    // 화면에 보여줄 게시물을 model에 저장
        return "resume/update";
    }*/



     // 게시물 수정폼(화면-Get)
    @GetMapping("/update/{resumeId}")
    public String updateResume(@PathVariable("resumeId") int resumeId,
                               Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 이력서 상세 정보 가져오기
        ResumeDto resumeDto = resumeService.findById(resumeId);

        // 현재 로그인한 사용자의 ID를 DTO에 설정
        String loggedInUsername = userDetails.getUsername();
        resumeDto.setJobSeekerId(loggedInUsername);

        // 화면에 보여줄 데이터를 model에 저장
        model.addAttribute("resume", resumeDto);

        return "resume/update"; // 수정 폼 페이지로 이동
    }

    //수정 전송
    @PostMapping("/update/{resumeId}")
    public String update(@PathVariable("resumeId") int resumeId, @ModelAttribute("resume") ResumeDto resumeDto,
                         @RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) throws Exception {

        // 현재 로그인한 사용자의 ID를 DTO에 설정
        String loggedInUsername = userDetails.getUsername();
        resumeDto.setJobSeekerId(loggedInUsername);

        if (resumeDto.getJobSeekerId() == null) {
            log.error("JobSeekerId가 null입니다. 업데이트할 수 없습니다.");
            return "redirect:/error"; // 또는 적절한 에러 처리
        }

        // 이력서 업데이트
        resumeService.updateResume(resumeDto, file);

        return "redirect:/resume/list"; // 업데이트 후 목록 페이지로 리다이렉션
    }

//    @GetMapping("/update/{resumeId}")
//    public String updateResume(@PathVariable("resumeId") int resumeId,
//                               Model model,
//                               @AuthenticationPrincipal CustomUserDetails userDetails) {
//
//        // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
//        String loggedInUsername = userDetails.getUsername();
//
//        // 이력서 정보를 가져옴
//        ResumeDto resumeDto = resumeService.findById(resumeId);
//
//        // 이력서가 null인지 확인
//        if (resumeDto == null) {
//            log.error("이력서를 찾을 수 없습니다. ID: " + resumeId);
//            return "redirect:/error"; // 또는 적절한 에러 처리
//        }
//
//        // jobSeekerId가 null인지 확인
//        if (resumeDto.getJobSeekerId() == null) {
//            log.error("이력서의 jobSeekerId가 null입니다. ID: " + resumeId);
//            return "redirect:/error"; // 또는 적절한 에러 처리
//        }
//
//        // 이력서가 현재 로그인한 사용자의 것인지 확인
//        if (!resumeDto.getJobSeekerId().equals(loggedInUsername)) {
//            log.error("사용자가 작성한 이력서가 아닙니다.");
//            return "redirect:/error"; // 또는 다른 적절한 처리
//        }
//
//        // jobSeekerId를 사용하여 JobSeekerVo를 조회
//        Optional<JobSeekerVo> optionalJobSeekerVo = jobSeekerService.getJobSeekerDetails(loggedInUsername);
//
//        if (optionalJobSeekerVo.isPresent()) {
//            model.addAttribute("jobSeekerVo", optionalJobSeekerVo.get());
//        } else {
//            log.error("JobSeekerVo를 찾을 수 없습니다.");
//            return "redirect:/error"; // 또는 다른 적절한 처리
//        }
//
//        model.addAttribute("resume", resumeDto);    // 화면에 보여줄 게시물을 model에 저장
//        return "resume/update";
//    }
//
//
//    /**
//     * 게시물 수정 처리(Post)
//     */
//    @PostMapping("/update/{resumeId}")
//    public String update(@ModelAttribute("resumeDto") ResumeDto resumeDto,
//                         @AuthenticationPrincipal CustomUserDetails userDetails,
//                         MultipartFile file) throws Exception {
//
//        // 현재 로그인한 사용자의 username(또는 ID)을 가져옴
//        String loggedInUsername = userDetails.getUsername();
//
//        // 만약 해당 사용자가 작성한 이력서가 아니면 접근을 막는 로직을 추가할 수 있습니다.
//        if (!resumeDto.getJobSeekerId().equals(loggedInUsername)) {
//            log.error("사용자가 작성한 이력서가 아닙니다.");
//            return "redirect:/error"; // 또는 다른 적절한 처리
//        }
//
//        // 파일 처리 로직
//        resumeService.updateResume(resumeDto, file);
//
//        return "redirect:/resume/list";
//    }

//
//    /**
//     * 게시물 수정폼(화면-Get)
//     */
//    @GetMapping("/update/{resumeId}")
//    public String updateResume(@PathVariable("resumeId") int resumeId,
//                                Model model) {
//
//        ResumeDto resumeDto = resumeService.findById(resumeId);
//        model.addAttribute("resume", resumeDto);    // 화면에 보여줄 게시물을 model에 저장
//        return "resume/update";
//    }
//
//    @PostMapping("/update/{resumeId}")
//    public String update(ResumeDto resumeDto, Model model) {
//        resumeService.updateResume(resumeDto);
//        ResumeDto dto = resumeService.findById(resumeDto.getResumeId());
//        model.addAttribute("resume", dto);
//        return "redirect:/list";
//
//    }




    @PostMapping("/delete/{resumeId}")
    public String deleteResume(@PathVariable("resumeId") int resumeId) {
        resumeService.deleteResume(resumeId);
        return "redirect:/resume/list";  // 삭제 후 목록 페이지로 리다이렉션
    }
}




