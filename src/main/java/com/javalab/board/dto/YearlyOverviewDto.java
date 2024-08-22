package com.javalab.board.dto;

import lombok.Data;

@Data
public class YearlyOverviewDto {
    private int year;                  // 연도
    private long totalRevenue;        // 총 수익
    private int totalApplications;    // 총 지원 수
    private int totalJobPosts;        // 총 공고 수
}
