package com.javalab.board.repository;

import com.javalab.board.vo.AdminVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    // Insert a new Admin
    void insertAdmin(AdminVo adminVo);

    // Select an Admin by ID
    AdminVo getAdminById(String adminId);

    // Select an Admin by email (for login or verification)
    AdminVo getAdminByEmail(String email);

    // Update Admin details
    void updateAdmin(AdminVo adminVo);

    // Delete an Admin by ID
    void deleteAdmin(String adminId);

    void findById(String adminId);
}