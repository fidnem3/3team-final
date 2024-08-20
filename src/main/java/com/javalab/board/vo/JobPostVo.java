package com.javalab.board.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
public class JobPostVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long jobPostId;
	private String compId; // 기업 ID
	private String title;
	private String content;
	private String position;
	private String salary;
	private String experience;
	private String education;
	private String address;
	private String homepage;
	private Integer hitNo;
	private String logoName;
	private String logoPath;
	private String status; // 'Pending', 'Approved', 'Rejected'
	private Long paymentId; // 결제 ID
	// 날짜 바인딩 패턴 : yyyy-MM-dd
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate ;
	private Date created;
	private String paymentStatus; // 'Before Payment', 'After Payment'


}
