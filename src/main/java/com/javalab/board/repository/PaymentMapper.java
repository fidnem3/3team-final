package com.javalab.board.repository;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.vo.PaymentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentDto paymentDto);
    void updatePayment(PaymentDto paymentDto);
    PaymentDto selectPaymentById(Long paymentId);
    List<PaymentDto> selectAllPayments();
}
