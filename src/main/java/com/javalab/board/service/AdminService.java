package com.javalab.board.service;

import com.javalab.board.vo.AdminVo;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    // 관리자 ID로 관리자 정보를 조회
    Optional<AdminVo> getAdminDetails(String adminId);

    // 관리자 ID와 비밀번호로 인증
    Optional<AdminVo> authenticateAdmin(String adminId, String password);

    void registerAdmin(AdminVo adminVo, UserRolesVo userRolesVo);

    List<JobSeekerVo> getAllJobSeekers();

    List<CompanyVo> getAllCompanies();
}
