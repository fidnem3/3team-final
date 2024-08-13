package com.javalab.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class PaymentDto {
        private Long paymentId;
        private String compId;
        private String accountNum;
        private Long jobPostId;
        private String paymentStatus;
        private Date paymentDate;
        private Date postDuration;
        private BigDecimal dailyRate;
        private BigDecimal totalAmount;
        private Date dueDate;

        // Getters and Setters
}
