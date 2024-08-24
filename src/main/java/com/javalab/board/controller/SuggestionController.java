package com.javalab.board.controller;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.dto.MemberDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.dto.SuggestionDto;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.service.*;
import com.javalab.board.vo.*;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.thymeleaf.TemplateEngine;

import java.io.*;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/suggestion")
public class SuggestionController {

    @Autowired
    private JobSeekerService jobSeekerService;
    @Autowired
    private SuggestionService suggestionService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createSuggestion(@ModelAttribute SuggestionDto suggestionDto, Principal principal) {
        Map<String, String> response = new HashMap<>();
        if (principal == null) {
            response.put("redirect", "/member/login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // DTO를 VO로 변환
        SuggestionVo suggestionVo = SuggestionVo.builder()
                .name(suggestionDto.getName())
                .tel(suggestionDto.getTel())
                .title(suggestionDto.getTitle())
                .content(suggestionDto.getContent())
                .build();

        // JobPost 저장
        Long sugId = suggestionService.saveSuggestion(suggestionVo);

        response.put("success", "true");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("/delete/{sugId}")
    public String deleteJobPost(@PathVariable("sugId") Long sugId) {
        suggestionService.deleteSuggestion(sugId);
        return "redirect:/admin/suggestionList";
    }

}