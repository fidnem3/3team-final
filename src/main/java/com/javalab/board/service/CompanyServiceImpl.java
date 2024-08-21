package com.javalab.board.service;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.repository.ApplicationMapper;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.UserRolesMapper;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.UserRolesVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    // 승인된 기업 목록을 저장할 리스트
    private List<CompanyVo> companyList = new ArrayList<>();





    @Override
    @Transactional
    public void registerCompany(CompanyVo companyVo, UserRolesVo userRolesVo) {
        // 1. 기업 회원의 상태를 "Pending"으로 설정합니다.
        companyVo.setStatus("Pending");

        // 기업 회원 정보를 등록합니다.
        companyMapper.insertCompany(companyVo);

        // UserRolesVo 객체가 null인 경우 새로 생성
        if (userRolesVo == null) {
            userRolesVo = new UserRolesVo();
        }

        // 필수 필드 설정
        if (userRolesVo.getUserId() == null || userRolesVo.getUserId().isEmpty()) {
            userRolesVo.setUserId(companyVo.getCompId());
        }
        if (userRolesVo.getUserType() == null || userRolesVo.getUserType().isEmpty()) {
            userRolesVo.setUserType("company");
        }
        if (userRolesVo.getRoleId() == null || userRolesVo.getRoleId().isEmpty()) {
            userRolesVo.setRoleId("ROLE_COMPANY"); // 적절한 기본 역할 ID
        }

        // UserRoles 테이블에 권한 정보를 추가합니다.
        userRolesMapper.insertUserRole(userRolesVo);
    }

    /**
     * 주어진 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param id 조회할 기업 회원의 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     */
    @Override
    public Optional<CompanyVo> getCompanyDetails(String id) {
        return Optional.ofNullable(companyMapper.selectCompanyById(id));
    }

    /**
     * 기업 회원 정보를 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    @Override
    @Transactional
    public CompanyVo updateCompany(CompanyVo companyVo) {
        companyMapper.updateCompany(companyVo);
        return companyMapper.selectCompanyById(companyVo.getCompId());
    }

    @Override
    @Transactional
    public void deleteCompany(String companyId) {
        // 1. UserRoles 테이블에서 관련 레코드 삭제
        userRolesMapper.deleteUserRole(companyId, "company", "ROLE_COMPANY");

        // 2. Company 테이블에서 레코드 삭제
        companyMapper.deleteCompany(companyId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = userRolesMapper.selectUserRole(username, "company")
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleId()))
                .collect(Collectors.toList());

        return new User(
                company.getCompId(),
                company.getPassword(),
                authorities
        );
    }

    @Override
    public CompanyVo getCompanyById(String compId) {
        return companyMapper.getCompanyById(compId);
    }

    @Override
    public List<CompanyVo> getPendingCompanies() {
        return companyMapper.selectCompaniesByStatus("Pending");
    }

    @Override
    @Transactional
    public void approveCompany(String compId) {
        // 기업 상태를 "Approved"로 업데이트
        companyMapper.updateCompanyStatus(compId, "Approved");
        log.info("compId " + compId);

        // 상태 업데이트 후 기업 정보를 조회
        CompanyVo updatedCompany = companyMapper.selectCompanyById(compId);

        // updatedCompany가 null이 아닌지 확인 후 상태를 출력
        if (updatedCompany != null) {
            System.out.println("Updated company status: " + updatedCompany.getStatus());
        } else {
            System.out.println("기업을 찾을 수 없습니다: ID " + compId);
        }
    }

    @Override
    @Transactional
    public void rejectCompany(String compId) {
        // 기업 상태를 "Rejected"로 업데이트
        companyMapper.updateCompanyStatus(compId, "Rejected");

        // 상태 업데이트 후 기업 정보를 조회
        CompanyVo updatedCompany = companyMapper.selectCompanyById(compId);

        // updatedCompany가 null이 아닌지 확인 후 상태를 출력
        if (updatedCompany != null) {
            System.out.println("Updated company status: " + updatedCompany.getStatus());
        } else {
            System.out.println("기업을 찾을 수 없습니다: ID " + compId);
        }
    }

    //알림 기능 시작

    @Override
    public boolean checkForUnreadApplications(String compId) {
        return applicationMapper.countUnreadApplications(compId) > 0;
    }

    @Override
    public List<ApplicationDto> getApplicationsByCompanyId(String compId) {
        return applicationMapper.selectApplicationsByCompanyId(compId);
    }

    @Override
    public void markApplicationAsRead(Long applicationId) {
        applicationMapper.markApplicationAsRead(applicationId);
    }

    @Override
    @Transactional
    public void addToCompanyList(CompanyVo companyVo) {
        // 기업을 목록에 추가합니다.
        companyMapper.addToCompanyList(companyVo);
        log.info("Company added to companyList: " + companyVo.getCompanyName());
    }



}
