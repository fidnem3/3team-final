package com.javalab.board.repository;

import com.javalab.board.vo.CompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}