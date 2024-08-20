package com.javalab.board.controller;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.service.ApplicationService;
import com.javalab.board.service.CompanyService;
import com.javalab.board.vo.CompanyVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationService applicationService;


    /**
     * 기업의 상세 정보를 보여주는 페이지로 이동합니다.
     * @param model Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return companyDetail.html 페이지
     */
    @GetMapping("/detail")
    public String getCompanyDetail(Model model, Authentication authentication) {
        String companyId = authentication.getName();
        CompanyVo company = companyService.getCompanyDetails(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        model.addAttribute("company", company);
        return "company/companyDetail";
    }

    /**
     * 기업 정보를 수정할 수 있는 페이지로 이동합니다.
     * @param model Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return companyUpdate.html 페이지
     */
    @GetMapping("/edit")
    public String editCompanyDetail(Model model, Authentication authentication) {
        String companyId = authentication.getName();
        CompanyVo company = companyService.getCompanyDetails(companyId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        model.addAttribute("company", company);
        return "company/companyUpdate";
    }

    /**
     * 기업 정보를 업데이트하고 상세보기 페이지로 리다이렉트합니다.
     * @param companyVo 수정된 기업 정보
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/update")
    public String updateCompanyDetail(@ModelAttribute("company") CompanyVo companyVo, RedirectAttributes redirectAttributes) {
        try {
            CompanyVo updatedCompany = companyService.updateCompany(companyVo);
            redirectAttributes.addFlashAttribute("message", "회사 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/company/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회사 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/company/edit";
        }
    }

    @GetMapping("/withdraw")
    public String showWithdrawForm() {
        return "company/withdrawConfirm";
    }

    @PostMapping("/withdraw")
    public String withdrawCompany(Authentication authentication, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String companyId = authentication.getName();
        try {
            companyService.deleteCompany(companyId);

            // 로그아웃 처리
            new SecurityContextLogoutHandler().logout(request, null, null);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "기업 회원 탈퇴가 정상적으로 완료되었습니다.");

            return "redirect:/member/login";  // 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 실패 시 에러 메시지 추가
            redirectAttributes.addFlashAttribute("error", "기업 회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/company/detail";
        }
    }



    //알림 기능 시작
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String companyId = authentication.getName();
        boolean hasUnreadApplications = companyService.checkForUnreadApplications(companyId);

        model.addAttribute("hasUnreadApplications", hasUnreadApplications);
        if (hasUnreadApplications) {
            model.addAttribute("notification", "새로운 이력서가 접수되었습니다.");
        }

        return "company/dashboard";
    }

    @PostMapping("/applications/read/{applicationId}")
    public String markApplicationAsRead(@PathVariable Long applicationId) {
        companyService.markApplicationAsRead(applicationId);
        return "redirect:/company/dashboard"; // 읽은 후 다시 대시보드로 리다이렉트
    }



    @GetMapping("/applications")
    public String viewCompanyApplications(Model model, Authentication authentication) {
        String companyId = authentication.getName();  // 로그인한 회사의 ID 가져오기

        System.out.println(companyId);

        // 회사의 공고에 지원한 이력서 목록 가져오기
        List<ApplicationDto> applications = applicationService.getApplicationsByCompanyId(companyId);

        model.addAttribute("applications", applications);
        return "company/companyApplications";  // 지원 내역을 보여줄 페이지
    }

//    @GetMapping("/applications/markAsRead/{applicationId}")
//    public String markApplicationAsRead(@PathVariable("applicationId") Long applicationId, Authentication authentication, RedirectAttributes redirectAttributes) {
//
//        String companyId = authentication.getName();  // 로그인한 회사의 ID 가져오기
//
//        companyService.markApplicationAsRead(applicationId);
//
//        redirectAttributes.addFlashAttribute("message", "이력서를 읽음 상태로 표시했습니다.");
//        return "redirect:/company/applications";  // 다시 이력서 목록으로 리다이렉트
//    }


    @GetMapping("/applications/markAsReadAndView/{resumeId}")
    public String markApplicationAsReadAndView(@PathVariable("resumeId") Long resumeId,
                                               @RequestParam("applicationId") Long applicationId,
                                               Authentication authentication,
                                               RedirectAttributes redirectAttributes) {

        String companyId = authentication.getName();  // 로그인한 회사의 ID 가져오기

        // 이력서를 읽음으로 표시
        companyService.markApplicationAsRead(applicationId);

        // 이력서 상세 페이지로 리다이렉트
        return "redirect:/resume/detail/" + resumeId;
    }




}