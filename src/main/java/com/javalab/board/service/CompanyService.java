package com.javalab.board.service;

import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface CompanyService extends UserDetailsService {
    /**
     * 새로운 기업 회원을 등록하고 권한을 추가합니다.
     * - @param companyVo 등록할 기업 회원 정보 객체
     * - @param userRolesVo 기업 회원의 권한 정보를 포함하는 객체
     */
    void registerCompany(CompanyVo companyVo, UserRolesVo userRolesVo);

    /**
     * 주어진 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param id 조회할 기업 회원의 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     */
    Optional<CompanyVo> getCompanyDetails(String id);

    /**
     * 기업 회원 정보를 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    void updateCompany(CompanyVo companyVo);

    /**
     * 주어진 ID에 해당하는 기업 회원 정보를 삭제합니다.
     * - @param id 삭제할 기업 회원의 ID
     */
    void deleteCompany(String id);

    /**
     * 사용자 이름으로 사용자 세부 정보를 로드합니다.
     * - @param username 사용자 이름
     * - @return UserDetails 객체
     */
    @Override
    UserDetails loadUserByUsername(String username);
}
