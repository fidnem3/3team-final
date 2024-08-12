package com.javalab.board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
public class PaymentDto {
        private Long paymentId;
        private String compId;
        private String accountNum;
        private Long jobPostId;
        private String paymentStatus;
        private Date paymentDate;
        private Date postDuration;
        private Double dailyRate;
        private Double totalAmount;
        private Date dueDate;
}

