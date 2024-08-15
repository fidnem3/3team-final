package com.javalab.board.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString

public class JobSeekerScrapVo {
    private Long scrapId; // 스크랩Id
    private String jobSeekerId; //유저아이디
    private Long jobPostId;
    private Date created; //스크랩날짜
    private Date endDate; //
    private String title;
    private String salary;
    private String address;

}
