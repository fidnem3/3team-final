package com.javalab.board.repository;

import com.javalab.board.vo.PaymentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentMapper {
    void insertPayment(PaymentVo paymentVo);

    void updatePaymentStatus(Map<String, Object> params);
}