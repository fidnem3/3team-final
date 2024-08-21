package com.javalab.board.controller;

import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String compId = userDetails.getUsername(); // 사용자 ID 가져오기
            String userType = userDetails.getUserType(); // 사용자 타입 (예: 회사, 구직자)

            if ("company".equalsIgnoreCase(userType)) {
                // 회사일 경우 읽지 않은 지원서 수 조회
                int unreadApplicationCount = notificationService.countUnreadApplications(compId);
                model.addAttribute("unreadApplicationCount", unreadApplicationCount);
            }
        }
    }
}