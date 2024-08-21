package com.javalab.board.repository;

import com.javalab.board.vo.CompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CompanyMapper {


    /**
     * 기업 회원 정보를 데이터베이스에 삽입합니다.
     * - @param companyVo 삽입할 기업 회원 정보 객체
     */
    void insertCompany(CompanyVo companyVo);

    /**
     * 주어진 ID에 해당하는 기업 회원 정보를 조회합니다.
     * - @param id 조회할 기업 회원의 ID
     * - @return 조회된 기업 회원 정보 객체
     */
     CompanyVo selectCompanyById(String compId);

    /**
     * 기업 회원 정보를 데이터베이스에서 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    void updateCompany(CompanyVo companyVo);

    /**
     * 주어진 회사 ID에 해당하는 기업 회원 정보를 데이터베이스에서 삭제합니다.
     * - @param compId 삭제할 기업 회원의 회사 ID
     */
    void deleteCompany(String compId);


    // 기업 ID로 기업 조회
    CompanyVo getCompanyById(String compId);

    //읽기 조회
    void markApplicationAsRead(Long applicationId);


    /**
     * 특정 상태를 가진 기업 목록을 조회합니다.
     * - @param status 기업의 상태 (예: "PENDING", "APPROVED", "REJECTED")
     * - @return 특정 상태를 가진 기업 목록
     */
        List<CompanyVo> selectCompaniesByStatus(@Param("status") String status);

    /**
     * 특정 기업의 상태를 업데이트합니다.
     * - @param compId 기업 ID
     * - @param status 새로운 상태 (예: "APPROVED", "REJECTED")
     */
    void updateCompanyStatus(@Param("compId") String compId, @Param("status") String status);



}