package com.javalab.board.controller;

import com.javalab.board.repository.LoginMapper;
import com.javalab.board.service.AdminService; // AdminService 추가
import com.javalab.board.service.CompanyService;
import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.plaf.multi.MultiTabbedPaneUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
@Log4j2
public class LoginController {

    private final CompanyService companyService;
    private final JobSeekerService jobSeekerService;
    private final AdminService adminService; // AdminService 추가
    private final LoginMapper loginMapper;  // LoginMapper 인스턴스 주입
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
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        // 유효성 검사 오류가 있는 경우
        if (bindingResult.hasErrors()) {
            return "member/companyJoin";
        }

        // 비밀번호 암호화
        companyVo.setPassword(passwordEncoder.encode(companyVo.getPassword()));

        // UserRolesVo 객체 생성 및 설정
        UserRolesVo userRolesVo = new UserRolesVo();
        userRolesVo.setUserId(companyVo.getCompId());
        userRolesVo.setUserType("company");
        userRolesVo.setRoleId("ROLE_COMPANY");

        try {
            // 파일 업로드 처리
            if (!file.isEmpty()) {
                // 업로드할 디렉토리 경로 설정
                String uploadDir = "C:\\filetest\\upload";
                Path uploadPath = Paths.get(uploadDir);

                // 디렉토리가 존재하지 않으면 생성
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 파일 저장
                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // CompanyVo에 파일 이름과 경로 설정
                companyVo.setLogoName(fileName);
                companyVo.setLogoPath(filePath.toString());
            }

            // 기업 등록 처리
            companyService.registerCompany(companyVo, userRolesVo);
            redirectAttributes.addFlashAttribute("message", "회원가입이 성공적으로 완료되었습니다. 관리자의 승인을 기다려 주세요.");
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



    // 소설 로그인
    @GetMapping("/modify")
    public String showModifyPage(@AuthenticationPrincipal Object principal, Model model) {
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            model.addAttribute("name", oauth2User.getAttributes().get("name"));
            model.addAttribute("birth", oauth2User.getAttributes().get("birth"));
            model.addAttribute("tel", oauth2User.getAttributes().get("tel"));
            model.addAttribute("address", oauth2User.getAttributes().get("address"));
        } else if (principal instanceof JobSeekerVo) {
            JobSeekerVo jobSeekerVo = (JobSeekerVo) principal;
            model.addAttribute("name", jobSeekerVo.getName());
            model.addAttribute("birth", jobSeekerVo.getBirth());
            model.addAttribute("tel", jobSeekerVo.getTel());
            model.addAttribute("address", jobSeekerVo.getAddress());
        }
        return "member/modify";
    }

    @PostMapping("/modify")
    public String modifyUser(
            @AuthenticationPrincipal Object principal,
            @RequestParam(name = "newPassword", required = false) String newPassword,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "birth", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birth,
            @RequestParam(name = "tel", required = false) String tel,
            @RequestParam(name = "address", required = false) String address) {

        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;

            // 비밀번호는 소셜 로그인 사용자에게는 처리하지 않음
            if (newPassword != null && !newPassword.isEmpty()) {
                // 비밀번호 변경 로직 (OAuth2 사용자에게는 외부 시스템을 통해 처리해야 할 수도 있음)
                // 예: 외부 API 호출을 통해 비밀번호 변경
                // 이 부분은 구체적인 외부 API나 서비스에 따라 다를 수 있습니다.
            }

            // OAuth2 사용자 정보를 내부 데이터베이스에 저장하거나 외부 시스템에 업데이트
            // 필요 시, OAuth2 사용자 정보를 업데이트할 수 있는 로직을 구현
            // 예: 외부 API 호출을 통해 정보 업데이트
        } else if (principal instanceof JobSeekerVo) {
            JobSeekerVo jobSeeker = (JobSeekerVo) principal;

            // 비밀번호 변경 처리
            if (newPassword != null && !newPassword.isEmpty()) {
                // 실제 비밀번호 암호화 로직 필요
                jobSeeker.setPassword(newPassword); // 비밀번호 암호화 로직을 추가해야 합니다.
            }

            // 사용자 정보 수정
            if (name != null && !name.isEmpty()) jobSeeker.setName(name);
            if (birth != null) jobSeeker.setBirth(birth); // birth가 null 체크 필요
            if (tel != null && !tel.isEmpty()) jobSeeker.setTel(tel);
            if (address != null && !address.isEmpty()) jobSeeker.setAddress(address);

            // 수정된 정보를 데이터베이스에 반영
            loginMapper.updateJobSeeker(jobSeeker); // LoginMapper 객체는 @Autowired로 주입받아야 합니다.
        }

        // 성공적으로 처리 후 리다이렉트
        return "redirect:/index"; // 수정 후 리다이렉트할 페이지
    }
}

