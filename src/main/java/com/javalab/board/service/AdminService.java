package com.javalab.board.service;

import com.javalab.board.repository.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public void getAdminById(String adminId) {
        adminMapper.findById(adminId);
    }
}
