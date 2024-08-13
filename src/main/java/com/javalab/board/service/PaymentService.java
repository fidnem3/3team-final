package com.javalab.board.service;

import com.javalab.board.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    void createPayment(PaymentDto paymentDto);
    void updatePayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(Long paymentId);
    List<PaymentDto> getAllPayments();
}
