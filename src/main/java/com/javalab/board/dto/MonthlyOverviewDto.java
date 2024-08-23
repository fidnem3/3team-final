package com.javalab.board.dto;


import lombok.Data;

@Data
public class MonthlyOverviewDto {
    private String month;
    private Long totalRevenue;
}
