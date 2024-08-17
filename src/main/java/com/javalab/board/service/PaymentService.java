package com.javalab.board.service;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.vo.PaymentVo;

import java.util.List;

public interface PaymentService {
//    void createPayment(PaymentDto paymentDto);
//    void updatePayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(Long paymentId);
    List<PaymentDto> getAllPayments();
    Long savePayment(PaymentVo paymentVo);
}
