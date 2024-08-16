package com.javalab.board.controller;

import com.javalab.board.service.AdminService; // AdminService 추가
import com.javalab.board.service.CompanyService;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.AdminVo; // AdminVo 추가
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
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
    private final AdminService adminService; // AdminService 추가
    private final PasswordEncoder passwordEncoder;

    // 로그인 화면
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        String errorMessage = (String) request.getSession().getAttribute("loginErrorMessage");
        String errorType = (String) request.getSession().getAttribute("loginErrorType");

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("errorType", errorType);
            request.getSession().removeAttribute("loginErrorMessage");
            request.getSession().removeAttribute("loginErrorType");
        }

        return "member/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String adminId, @RequestParam String password, RedirectAttributes redirectAttributes) {
        AdminVo admin = adminService.authenticateAdmin(adminId, password)
                .orElse(null);

        if (admin != null) {
            // 로그인 성공 시 관리자 페이지로 리다이렉트
            return "redirect:/member/adminPage";
        } else {
            // 로그인 실패 시 오류 메시지 추가
            redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
        }
    }


    // 구직자 페이지
    @GetMapping("/jobSeekerPage")
    public String jobSeekerPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        JobSeekerVo jobSeeker = jobSeekerService.getJobSeekerDetails(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        model.addAttribute("jobSeeker", jobSeeker);
        return "index"; // Thymeleaf 템플릿 파일 이름
    }

    // 기업 페이지
    @GetMapping("/companyPage")
    public String companyPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        CompanyVo company = companyService.getCompanyDetails(username)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with username: " + username));
        model.addAttribute("company", company);
        return "index"; // Thymeleaf 템플릿 파일 이름
    }

    // 관리자 페이지
    @GetMapping("/adminPage")
    public String adminPage(Model model, Authentication authentication) {
        // 관리자의 정보를 모델에 추가 (필요한 경우)
        // 예: model.addAttribute("admin", adminService.getAdminDetails(authentication.getName()));
        return "member/adminPage"; // Thymeleaf 템플릿 파일 이름
    }

    // 회원가입 페이지
    @GetMapping("/classification")
    public String classificationPage() {
        return "member/classification";
    }

    // 관리자 회원가입 페이지
    @GetMapping("/adminJoin")
    public String adminJoinPage(Model model) {
        model.addAttribute("AdminVo", new AdminVo());
        model.addAttribute("UserRolesVo", new UserRolesVo());
        return "member/adminJoin";
    }

    // 관리자 회원가입 처리
    @PostMapping("/adminJoin")
    public String registerAdmin(@Valid @ModelAttribute("AdminVo") AdminVo adminVo,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "member/adminJoin";
        }

        // 비밀번호를 암호화합니다.
        adminVo.setPassword(passwordEncoder.encode(adminVo.getPassword()));

        UserRolesVo userRolesVo = new UserRolesVo();
        userRolesVo.setUserId(adminVo.getAdminId());
        userRolesVo.setUserType("admin");
        userRolesVo.setRoleId("ROLE_ADMIN"); // 또는 적절한 기본 역할 ID

        try {
            // 관리자 회원가입 처리
            adminService.registerAdmin(adminVo, userRolesVo);
            redirectAttributes.addFlashAttribute("message", "관리자 회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/member/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/member/adminJoin";
        }
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
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {
        // 유효성 검사 오류가 있는 경우, 다시 회원가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "member/companyJoin";
        }

        // 비밀번호를 암호화합니다.
        companyVo.setPassword(passwordEncoder.encode(companyVo.getPassword()));

        // UserRolesVo 객체 생성 및 설정합니다.
        UserRolesVo userRolesVo = new UserRolesVo();
        userRolesVo.setUserId(companyVo.getCompId());
        userRolesVo.setUserType("company");
        userRolesVo.setRoleId("ROLE_COMPANY"); // 또는 적절한 기본 역할 ID

        try {
            companyService.registerCompany(companyVo, userRolesVo);
            // 기업 회원가입 처리
            redirectAttributes.addFlashAttribute("message", "기업 회원가입이 성공적으로 완료되었습니다.");
            log.info("회원가입 성공: {}", companyVo.getCompId());
            return "redirect:/member/login";
        } catch (Exception e) {
            bindingResult.reject("registerError", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            log.error("회원가입 실패: {}", e.getMessage());
            return "redirect:/member/companyJoin";
        }
    }


    // 개인회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("JobSeekerVo", new JobSeekerVo());
        model.addAttribute("UserRolesVo", new UserRolesVo()); // 추가
        return "member/join";
    }


    @PostMapping("/join")
    public String registerJobSeeker(@Valid @ModelAttribute("JobSeekerVo") JobSeekerVo jobSeekerVo,
                                    BindingResult bindingResult,
                                    Model model,
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

        // UserRolesVo 객체 생성 및 설정
        UserRolesVo userRolesVo = new UserRolesVo();
        userRolesVo.setUserId(jobSeekerVo.getJobSeekerId());
        userRolesVo.setUserType("jobSeeker");
        userRolesVo.setRoleId("ROLE_USER");

        try {
            // 개인 회원가입 처리
            jobSeekerService.registerJobSeeker(jobSeekerVo, userRolesVo);
            redirectAttributes.addFlashAttribute("message", "개인 회원가입이 성공적으로 완료되었습니다.");
            return "redirect:/member/login";
        } catch (Exception e) {
            // 예외 처리
            bindingResult.reject("registerError", "회원가입 처리 중 오류가 발생했습니다: " + e.getMessage());
            return "member/join";
        }
    }

}




//    // 카카오 소셜 로그인 사용자 비밀번호 변경 화면
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

