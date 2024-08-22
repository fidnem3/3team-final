package com.javalab.board.service;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.vo.PaymentVo;

import java.util.List;
import java.util.Map;

public interface PaymentService {
//    void createPayment(PaymentDto paymentDto);
//    void updatePayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(Long paymentId);
    List<PaymentDto> getAllPayments();
    Long savePayment(PaymentVo paymentVo);


    List<Map<Long, Integer>> getTotalPaymentsForAllJobPosts();



}
