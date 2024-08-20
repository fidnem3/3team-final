package com.javalab.board.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Long applicationId;  // 지원 ID
    private int resumeId;       // 이력서 ID
    private Long jobPostId;      // 구직공고 ID
    private String jobSeekerId;  // 지원자 ID
    private String isRead;       // 지원 상태
    private Date appliedDate;    // 지원 일자
    private Date created;


}
