package com.javalab.board.dto;

import com.javalab.board.vo.JobSeekerVo;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class ResumeDto {
        private int resumeId; // 이력서ID
        private String jobSeekerId; // 유저아이디
        private String title; // 제목
        private String content; //내용
        private String education; // 학력
        private String experience; // 경력
        private String link; // 링크
        private int hitNo; // 조회수
        private String fileName; // 파일명
        private String filePath; // 파일주소
        private List<String> skills;
        private JobSeekerVo jobSeekerVo;
        private MultipartFile resumeFile;
        private int fileAttached;

        // 추가: 기술들을 쉼표로 구분된 문자열로 변환하는 메서드
        public String getSkillsAsString() {
                if (skills != null && !skills.isEmpty()) {
                        return String.join(", ", skills);
                }
                return "";
        }
}

