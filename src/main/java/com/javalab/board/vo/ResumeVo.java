package com.javalab.board.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class ResumeVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
}
