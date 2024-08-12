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

        // UserRoles 테이블에 권한 정보를 추가합니다.
        userRolesMapper.insertUserRole(userRolesVo);
    }

    @Override
    public Optional<CompanyVo> getCompanyDetails(String id) {
        return Optional.ofNullable(companyMapper.selectCompanyById(id));
    }

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
        // 회사 정보 조회 로직
        CompanyVo company = companyMapper.selectCompanyById(username);
        if (company == null) {
            throw new UsernameNotFoundException("Company not found with username: " + username);
        }

        // 권한 정보를 조회
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
}
