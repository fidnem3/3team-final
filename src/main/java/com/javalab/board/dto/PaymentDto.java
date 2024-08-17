package com.javalab.board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@ToString
@Data
public class PaymentDto {
        private Long paymentId;
        private String compId;
        private Long jobPostId;
        private LocalDate paymentDate;
        private Long amount;
}