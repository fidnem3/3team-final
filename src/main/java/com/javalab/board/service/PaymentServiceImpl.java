package com.javalab.board.service;

import com.javalab.board.dto.PaymentDto;
import com.javalab.board.repository.PaymentMapper;
import com.javalab.board.service.PaymentService;
import com.javalab.board.vo.PaymentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public void createPayment(PaymentVo paymentVo) {
        paymentMapper.insertPayment(paymentVo);
    }

    @Override
    public void updatePaymentStatus(Long paymentId, String paymentStatus) {
        paymentMapper.updatePaymentStatus(Map.of("paymentId", paymentId, "paymentStatus", paymentStatus));
    }
}
