package com.javalab.board.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString

public class JobSeekerScrapVo {
    private Long scrapId; // 스크랩Id
    private String jobSeekerId; //유저아이디
    private Long jobPostId;
    private Date created; //스크랩날짜

}
