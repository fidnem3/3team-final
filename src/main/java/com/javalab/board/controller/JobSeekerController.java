package com.javalab.board.controller;

import com.javalab.board.service.JobSeekerService;
import com.javalab.board.vo.JobSeekerVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/jobSeeker")
public class JobSeekerController {

    @Autowired
    private JobSeekerService jobSeekerService;

    @Autowired
    private JobSeekerVo jobSeekerVo;

    private static final String UPLOAD_DIR = "C:/filetest/upload/";

    /**
     * 구직자의 상세 정보를 보여주는 페이지로 이동합니다.
     *
     * @param model          Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return jobSeekerDetail.html 페이지
     */
    @GetMapping("/detail")
    public String getJobSeekerDetail(Model model, Authentication authentication) {
        String jobSeekerId = authentication.getName();
        JobSeekerVo jobSeeker = jobSeekerService.getJobSeekerDetails(jobSeekerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        model.addAttribute("jobSeeker", jobSeeker);
        return "jobSeeker/jobSeekerDetail";
    }

    @GetMapping("/profile/{profileName}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String profileName) throws IOException {
        Path file = Paths.get(UPLOAD_DIR).resolve(profileName);
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
     * 구직자 정보를 수정할 수 있는 페이지로 이동합니다.
     *
     * @param model          Spring의 Model 객체
     * @param authentication 현재 인증된 사용자 정보
     * @return jobSeekerUpdate.html 페이지
     */
    @GetMapping("/edit")
    public String editJobSeekerDetail(Model model, Authentication authentication) {
        String jobSeekerId = authentication.getName();
        JobSeekerVo jobSeeker = jobSeekerService.getJobSeekerDetails(jobSeekerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        model.addAttribute("jobSeeker", jobSeeker);
        return "jobSeeker/jobSeekerUpdate";
    }

    /**
     * 구직자 정보를 업데이트하고 상세보기 페이지로 리다이렉트합니다.
     *
     * @param jobSeekerVo        수정된 구직자 정보
     * @param redirectAttributes 리다이렉트 시 메시지 전달을 위한 객체
     * @return 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/update")
    public String updateJobSeekerDetail(@ModelAttribute("jobSeeker") JobSeekerVo jobSeekerVo,
                                        @RequestParam(value = "profilePicFile", required = false) MultipartFile profilePicFile,
                                        RedirectAttributes redirectAttributes,
                                        Authentication authentication) {
        try {
            // 현재 로그인한 구직자의 ID 가져오기
            String jobSeekerId = authentication.getName();

            // 기존 구직자 정보 가져오기
            JobSeekerVo existingJobSeeker = jobSeekerService.getJobSeekerDetails(jobSeekerId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

            // 새 프로필 사진 파일이 업로드된 경우
            if (profilePicFile != null && !profilePicFile.isEmpty()) {
                // 기존 프로필 사진 파일 삭제
                if (existingJobSeeker.getFilePath() != null) {
                    Path existingFilePath = Paths.get(existingJobSeeker.getFilePath());
                    Files.deleteIfExists(existingFilePath);
                }

                // 새 프로필 사진 파일 저장
                String uploadDir = "C:\\filetest\\upload";  // 파일 업로드 디렉토리 경로
                Path uploadPath = Paths.get(uploadDir);

                // 업로드 디렉토리가 존재하지 않으면 생성
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 파일 이름 생성
                String fileName = UUID.randomUUID().toString() + "_" + profilePicFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                profilePicFile.transferTo(filePath.toFile());

                // 새 프로필 정보 설정
                jobSeekerVo.setFileName(fileName);
                jobSeekerVo.setFilePath(filePath.toString());
            } else {
                // 새 프로필 파일이 업로드되지 않은 경우, 기존 프로필 정보 유지
                jobSeekerVo.setFileName(existingJobSeeker.getFileName());
                jobSeekerVo.setFilePath(existingJobSeeker.getFilePath());
            }

            // 구직자 ID 설정 (보안상의 이유로 폼에서 전송된 ID 대신 인증 정보의 ID 사용)
            jobSeekerVo.setJobSeekerId(jobSeekerId);

            // 구직자 정보 업데이트
            JobSeekerVo updatedJobSeeker = jobSeekerService.updateJobSeeker(jobSeekerVo);
            redirectAttributes.addFlashAttribute("message", "구직자 정보가 성공적으로 업데이트되었습니다.");
            return "redirect:/jobSeeker/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "구직자 정보 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/jobSeeker/edit";
        }
    }


    @GetMapping("/withdraw")
    public String showWithdrawForm() {
        return "jobSeeker/withdrawConfirm";
    }

    @PostMapping("/withdraw")
    public String withdrawJobSeeker(Authentication authentication, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String jobSeekerId = authentication.getName();
        try {
            jobSeekerService.deleteJobSeeker(jobSeekerId);

            // 로그아웃 처리
            new SecurityContextLogoutHandler().logout(request, null, null);

            // 성공 메시지 추가
            redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 정상적으로 완료되었습니다.");

            return "redirect:/member/login";  // 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 실패 시 에러 메시지 추가
            redirectAttributes.addFlashAttribute("error", "회원 탈퇴 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/jobSeeker/detail";
        }
    }
}
