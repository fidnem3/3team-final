package com.javalab.board.controller;

import com.javalab.board.service.CompanyService;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
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

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
@Log4j2
public class LoginController {

    private final CompanyService companyService;
    private final JobSeekerService jobSeekerService;
    private final PasswordEncoder passwordEncoder;

    // 로그인 화면
    @GetMapping(value = "/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception) {
        log.info("MemberController loginMember 메소드");

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "member/login";
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
        model.addAttribute("UserRolesVo", new UserRolesVo());
        return "member/companyJoin";
    }

    // 기업회원 처리
    @PostMapping("/companyJoin")
    public String registerCompany(@Valid @ModelAttribute("CompanyVo") CompanyVo companyVo,
                                  @Valid @ModelAttribute("UserRolesVo") UserRolesVo userRolesVo,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있는 경우, 다시 회원가입 페이지로 이동
            return "member/companyJoin";
        }

        // 비밀번호 암호화
        companyVo.setPassword(passwordEncoder.encode(companyVo.getPassword()));

        companyService.registerCompany(companyVo, userRolesVo);
        redirectAttributes.addFlashAttribute("message", "기업 회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/member/login";
    }


    // 개인회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("JobSeekerVo", new JobSeekerVo());
        model.addAttribute("UserRolesVo", new UserRolesVo()); // 추가
        return "member/join";
    }


    // 개인회원 처리
    @PostMapping("/join")
    public String registerJobSeeker(@Valid @ModelAttribute("JobSeekerVo") JobSeekerVo jobSeekerVo,
                                    @Valid @ModelAttribute("UserRolesVo") UserRolesVo userRolesVo, // 추가
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
        jobSeekerService.registerJobSeeker(jobSeekerVo, userRolesVo);
        redirectAttributes.addFlashAttribute("message", "개인 회원가입이 성공적으로 완료되었습니다.");
        return "redirect:/member/login";
    }

}








    // 회원 가입 화면
//    @GetMapping(value = "/join")
//    public String memberForm(Model model){
//        model.addAttribute("memberFormDto", new MemberFormDto());
//        return "member/join";
//    }

    // 회원 가입 처리
//    @PostMapping(value = "/join")
//    public String newMember(@Valid MemberFormDto memberFormDto,
//                            BindingResult bindingResult,
//                            Model model){
//
//        if(bindingResult.hasErrors()){
//            log.info("회원가입 데이터 검증 오류 있음");
//            return "member/join";
//        }
//
//        try {
//            MemberVo member = MemberVo.builder()
//                    .memberId(memberFormDto.getEmail()) // 이메일을 memberId로 사용
//                    .password(passwordEncoder.encode(memberFormDto.getPassword()))
//                    .name(memberFormDto.getName())
//                    .email(memberFormDto.getEmail())
//                    .roles(List.of("ROLE_USER"))
//                    .build();
//
//            log.info("회원가입 데이터 member : " + member);
//            memberService.saveMember(member);
//        } catch (IllegalStateException e){
//            model.addAttribute("errorMessage", e.getMessage());
//            log.info("MemberController 회원가입시 중복 오류 : " + e.getMessage());
//            return "member/join";
//        }
//
//        return "redirect:/member/login"; //회원 가입 후 로그인
//    }



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

