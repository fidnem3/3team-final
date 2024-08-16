package com.javalab.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder // 빌더패턴적용
public class CreateJobPostRequestDto {

    private Long jobPostId;
    private String title;
    private String content;
    private String position;
    private String salary;
    private String experience;
    private String education;
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String homepage;
    private Integer hitNo;
    private String status;
    private String logoPath; // Path to the image
    private String paymentStatus; // 'Before Payment', 'After Payment'
}
