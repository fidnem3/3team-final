package com.javalab.board.controller;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.service.ApplicationService;
import com.javalab.board.service.CompanyService;
import com.javalab.board.vo.CompanyVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationService applicationService;

    private static final String UPLOAD_DIR = "C:/filetest/upload/";

    /**
     * 기업의 상세 정보를 보여주는 페이지로 이동합니다.
     * @param model Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return companyDetail.html 페이지
     */
    @GetMapping("/detail")
    public String getCompanyDetail(Model model, Authentication authentication) {
        String compId = authentication.getName();
        CompanyVo company = companyService.getCompanyDetails(compId)
                .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

        CompanyVo companyVo = companyService.getCompanyById(compId);
        String logoPath = companyVo != null ? companyVo.getLogoPath() : null;
        String logoName = companyVo != null ? companyVo.getLogoName() : null;

        model.addAttribute("logoName", logoName);

        model.addAttribute("company", company);
        return "company/companyDetail";
    }

    /**
     * 회사 로고 이미지를 제공하는 엔드포인트
     * @param logoName 로고 파일 이름
     * @return 로고 이미지 파일
     */
    @GetMapping("/logo/{logoName}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String logoName) throws IOException {
        Path file = Paths.get(UPLOAD_DIR).resolve(logoName);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new RuntimeException("Could not read the file!");
        }
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
    public String updateCompanyDetail(@ModelAttribute("company") CompanyVo companyVo,
                                      @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
                                      RedirectAttributes redirectAttributes,
                                      Authentication authentication) {
        try {
            // 현재 로그인한 회사의 ID 가져오기
            String companyId = authentication.getName();

            // 기존 회사 정보 가져오기
            CompanyVo existingCompany = companyService.getCompanyDetails(companyId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 기업을 찾을 수 없습니다."));

            // 새 로고 파일이 업로드된 경우
            if (logoFile != null && !logoFile.isEmpty()) {
                // 기존 로고 파일 삭제
                if (existingCompany.getLogoPath() != null) {
                    Files.deleteIfExists(Paths.get(existingCompany.getLogoPath()));
                }

                // 새 로고 파일 저장
                String uploadDir = "C:\\filetest\\upload";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = UUID.randomUUID().toString() + "_" + logoFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                logoFile.transferTo(filePath.toFile());

                // 새 로고 정보 설정
                companyVo.setLogoName(fileName);
                companyVo.setLogoPath(filePath.toString());
            } else {
                // 새 로고 파일이 업로드되지 않은 경우, 기존 로고 정보 유지
                companyVo.setLogoName(existingCompany.getLogoName());
                companyVo.setLogoPath(existingCompany.getLogoPath());
            }

            // 회사 ID 설정 (보안상의 이유로 폼에서 전송된 ID 대신 인증 정보의 ID 사용)
            companyVo.setCompId(companyId);

            // 회사 정보 업데이트
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