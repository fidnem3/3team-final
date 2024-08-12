package com.javalab.board.service;

import com.javalab.board.vo.CompanyVo;

import java.util.Optional;

public interface CompanyService {

    /**
     * 새로운 기업 회원을 등록합니다.
     * - @param companyVo 등록할 기업 회원 정보 객체
     */
    void registerCompany(CompanyVo companyVo);

    /**
     * 주어진 회사 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param compId 조회할 기업 회원의 회사 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     * - Optional은 데이터베이스 조회 메서드에서 널 값을 처리할 때 유용하게 사용됩니다.
     * 특히, 조회 결과가 존재하지 않을 수 있는 경우에 적합합니다.
     */
    Optional<CompanyVo> getCompanyDetails(String compId);

    /**
     * 기업 회원 정보를 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    void updateCompany(CompanyVo companyVo);

    /**
     * 주어진 회사 ID에 해당하는 기업 회원 정보를 삭제합니다.
     * - @param compId 삭제할 기업 회원의 회사 ID
     */
    void deleteCompany(String compId);
}