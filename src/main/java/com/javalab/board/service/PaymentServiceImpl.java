package com.javalab.board.service;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.repository.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public void createPayment(PaymentDto paymentDto) {
        // Calculate total amount: (공고 기한 * 300원)
        long durationInDays = (paymentDto.getPostDuration().getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24);
        BigDecimal dailyRate = new BigDecimal("300");
        BigDecimal totalAmount = dailyRate.multiply(new BigDecimal(durationInDays));

        paymentDto.setDailyRate(dailyRate);
        paymentDto.setTotalAmount(totalAmount);
        paymentDto.setPaymentStatus("Before Payment");  // 초기 상태
        paymentMapper.insertPayment(paymentDto);
    }

    @Override
    public void updatePayment(PaymentDto paymentDto) {
        paymentDto.setPaymentStatus("After Payment"); // 결제 완료 후 상태 업데이트
        paymentMapper.updatePayment(paymentDto);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        return paymentMapper.selectPaymentById(paymentId);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentMapper.selectAllPayments();
    }
}
