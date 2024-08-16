package com.javalab.board.dto;

import java.sql.Date;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class JobSeekerScrapDto {
    private Long scrapId; // 스크랩Id
    private String jobSeekerId; //유저아이디
    private Long jobPostId;
    private Date created; //스크랩날짜

}
