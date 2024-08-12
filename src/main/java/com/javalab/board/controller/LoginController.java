package com.javalab.board.controller;

import com.javalab.board.service.CompanyService;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
@Log4j2
public class LoginController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    // 로그인 화면
    @GetMapping(value = "/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error) {
        log.info("MemberController login 메소드");

        if (error != null) {
            model.addAttribute("error", true); // 오류가 있을 경우 true로 설정
            model.addAttribute("errorMessage", "아이디 및 비밀번호를 확인하세요."); // 고정된 오류 메시지 설정
        }

        return "member/login"; // "member/login" 템플릿을 반환
    }

    // 회원가입 페이지
    @GetMapping("/classification")
    public String classificationPage() {
        return "member/classification";
    }


    // 기업회원가입 페이지
    @GetMapping("/companyJoin")
    public String companyJoinPage(Model model) {
        model.addAttribute("CompanyVo", new CompanyVo());
        return "member/companyJoin";
    }

    // 기업회원 처리
    @PostMapping("/companyJoin")
    public String registerCompany(@Valid @ModelAttribute("CompanyVo") CompanyVo companyVo,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있는 경우, 다시 회원가입 페이지로 이동
            return "member/companyJoin";
        }

        // 서비스 계층에서 회원가입 처리
        companyService.registerCompany(companyVo);
        redirectAttributes.addFlashAttribute("message", "기업 회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/member/login";
    }


    // 개인회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("JobSeekerVo", new JobSeekerVo());
        return "member/join";
    }


    // 개인회원 처리
    @PostMapping("/join")
    public String registerJobSeeker(@Valid @ModelAttribute("JobSeekerVo") JobSeekerVo jobSeekerVo,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!jobSeekerVo.getPassword().equals(jobSeekerVo.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "비밀번호가 일치하지 않습니다.");
        }

        // 유효성 검사 오류가 있는 경우, 다시 회원가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "member/join";
        }

        // 비밀번호를 암호화합니다.
        jobSeekerVo.setPassword(passwordEncoder.encode(jobSeekerVo.getPassword()));

        // 개인 회원가입 처리
        jobSeekerService.registerJobSeeker(jobSeekerVo);
        redirectAttributes.addFlashAttribute("message", "개인 회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/member/login";
    }

}


    // 카카오 소셜 로그인 사용자 비밀번호 변경 화면
//    @GetMapping("/modify")
//    public String modifyGET() {
//        log.info("modify get...");
//        return "member/modify";
//    }

    // 카카오 소셜 로그인 사용자 비밀번호+social 변경
//    @PostMapping("/modify")
//    public String modifyPOST(@AuthenticationPrincipal MemberVo memberVo,
//                             @RequestParam("newPassword") String newPassword,
//                             RedirectAttributes redirectAttributes) {
//
//        log.info("여기는 컨트롤러의 비밀번호 변경 메소드......email : " + memberVo.getEmail());
//
//        String encodedPassword = passwordEncoder.encode(newPassword);
//
//        // 화면에서 입력한 비밀번호 변경 및 social 상태 변경
//        memberService.modifyPasswordAndSocialStatus(memberVo.getEmail(), encodedPassword);
//
//        redirectAttributes.addFlashAttribute("result", "비밀번호 변경 성공");
//        return "redirect:/board/list"; // 비밀번호 변경 후 리다이렉트할 URL을 선택합니다.
//    }

