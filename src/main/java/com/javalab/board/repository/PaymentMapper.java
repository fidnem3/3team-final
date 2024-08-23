package com.javalab.board.repository;

import com.javalab.board.dto.MonthlyOverviewDto;
import com.javalab.board.dto.PaymentDto;
import com.javalab.board.vo.PaymentVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper {
    void updatePayment(PaymentDto paymentDto);

    PaymentDto selectPaymentById(Long paymentId);

    List<PaymentDto> selectAllPayments();

    void insertPayment(PaymentVo paymentVo);


    // 모든 공고에 대한 총 결제 금액 조회
    List<Map<Long, Integer>>findTotalPaymentsForAllJobPosts();

    List<MonthlyOverviewDto> findMonthlyOverview(@Param("year") int year);
}
