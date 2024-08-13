package com.javalab.board.service;

import com.javalab.board.repository.AdminMapper; // Admin 정보를 조회할 Mapper
import com.javalab.board.vo.AdminVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper; // AdminMapper 주입

    @Override
    public Optional<AdminVo> getAdminDetails(String adminId) {
        // Admin 정보를 DB에서 조회하는 로직
        return adminMapper.getAdminById(adminId); // Mapper 메서드 호출
    }

    // 관리자 ID와 비밀번호로 인증하는 메서드 추가
    public Optional<AdminVo> authenticateAdmin(String adminId, String password) {
        return adminMapper.getAdminByIdAndPassword(adminId, password); // 관리자 인증
    }
}
