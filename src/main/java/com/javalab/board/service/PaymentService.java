package com.javalab.board.service;

import com.javalab.board.vo.PaymentVo;

public interface PaymentService {
    void createPayment(PaymentVo paymentDto);
    void updatePaymentStatus(Long paymentId, String paymentStatus);
}