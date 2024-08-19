package com.javalab.board.service;

import com.javalab.board.repository.AdminMapper; // Admin 정보를 조회할 Mapper
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.UserRolesMapper;
import com.javalab.board.vo.AdminVo;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private  AdminMapper adminMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private CompanyMapper companyMapper;


    @Autowired
    private BlacklistService blacklistService;

    @Override
    public Optional<AdminVo> getAdminDetails(String adminId) {
        // Admin 정보를 DB에서 조회하는 로직
        return adminMapper.getAdminById(adminId); // Mapper 메서드 호출
    }

    // 관리자 ID와 비밀번호로 인증하는 메서드 추가
    public Optional<AdminVo> authenticateAdmin(String adminId, String password) {
        return adminMapper.getAdminByIdAndPassword(adminId, password); // 관리자 인증
    }

    @Override
    public void registerAdmin(AdminVo adminVo, UserRolesVo userRolesVo) {
        adminMapper.insertAdmin(adminVo);

        if (userRolesVo == null) {
            userRolesVo = new UserRolesVo();
        }
        // 필수 필드 설정
        if (userRolesVo.getUserId() == null || userRolesVo.getUserId().isEmpty()) {
            userRolesVo.setUserId(adminVo.getAdminId());
        }
        if (userRolesVo.getUserType() == null || userRolesVo.getUserType().isEmpty()) {
            userRolesVo.setUserType("admin");
        }
        if (userRolesVo.getRoleId() == null || userRolesVo.getRoleId().isEmpty()) {
            userRolesVo.setRoleId("ROLE_ADMIN"); // 또는 적절한 기본 역할 ID
        }

        // UserRoles 테이블에 권한 정보를 추가합니다.
        userRolesMapper.insertUserRole(userRolesVo);
    }

    @Override
    public List<JobSeekerVo> getAllJobSeekers() {
        List<JobSeekerVo> jobSeekers = adminMapper.selectAllJobSeekers();
        for (JobSeekerVo jobSeeker : jobSeekers) {
            jobSeeker.setBlacklisted(blacklistService.isBlacklisted(jobSeeker.getJobSeekerId(), "jobSeeker"));
        }
        return jobSeekers;
    }

    @Override
    public List<CompanyVo> getAllCompanies() {
        List<CompanyVo> companies = adminMapper.selectAllCompany();
        for (CompanyVo company : companies) {
            company.setBlacklisted(blacklistService.isBlacklisted(company.getCompId(), "company"));
        }
        return companies;
    }
}
