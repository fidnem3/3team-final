package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.repository.UserRolesMapper;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    @Override
    @Transactional
    public void registerCompany(CompanyVo companyVo, UserRolesVo userRolesVo) {
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
            userRolesVo.setRoleId("ROLE_COMPANY"); // 또는 적절한 기본 역할 ID
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
    public void updateCompany(CompanyVo companyVo) {
        companyMapper.updateCompany(companyVo);
    }

    @Override
    @Transactional
    public void deleteCompany(String id) {
        companyMapper.deleteCompany(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company == null) {
            // 기업이 아닌 경우 null을 반환
            return null;
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
}
