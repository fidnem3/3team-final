package com.javalab.board.service;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.UserRolesVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface CompanyService extends UserDetailsService {


    /**
     * 새로운 기업 회원을 등록합니다.
     * - @param companyVo 등록할 기업 회원 정보 객체
     */
    void registerCompany(CompanyVo companyVo, UserRolesVo userRolesVo);

    /**
     * 주어진 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param id 조회할 기업 회원의 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     * - Optional 은 데이터베이스 조회 메서드에서 널 값을 처리할 때 유용하게 사용됩니다.
     * - 특히, 조회 결과가 존재하지 않을 수 있는 경우에 적합합니다.
     */
    Optional<CompanyVo> getCompanyDetails(String id);

    /**
     * 기업 회원 정보를 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    CompanyVo updateCompany(CompanyVo companyVo);

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


    /**
     * 주어진 기업 ID에 해당하는 기업 정보를 조회합니다.
     * - @param compId 조회할 기업 회원의 ID
     * - @return 조회된 기업 정보 객체
     */
    CompanyVo getCompanyById(String compId);


    //알림 기능 시작

    /**
     * 기업의 compId로 읽지 않은 이력서가 있는지 확인하는 메서드.
     * @param compId 기업 ID
     * @return 읽지 않은 이력서가 있으면 true, 없으면 false
     */
    boolean checkForUnreadApplications(String compId);

    /**
     * 기업의 compId로 모든 이력서를 조회하는 메서드.
     * @param compId 기업 ID
     * @return 해당 기업으로 제출된 모든 이력서 목록
     */
    List<ApplicationDto> getApplicationsByCompanyId(String compId);

    /**
     * 특정 이력서를 읽음으로 표시하는 메서드.
     * @param applicationId 이력서 ID
     */
    void markApplicationAsRead(Long applicationId);

}