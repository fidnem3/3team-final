package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.vo.CompanyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 새로운 기업 회원을 등록합니다.
     * - @param companyVo 등록할 기업 회원 정보 객체
     */
    @Override
    public void registerCompany(CompanyVo companyVo) {
        // 비밀번호 암호화
        companyVo.setPassword(passwordEncoder.encode(companyVo.getPassword()));

        // 기본 역할 설정
        companyVo.setRole("COMPANY");

        // 데이터베이스에 저장
        companyMapper.insertCompany(companyVo);
    }

    /**
     * 주어진 회사 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param compId 조회할 기업 회원의 회사 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     */
    @Override
    public Optional<CompanyVo> getCompanyDetails(String compId) {
        return Optional.ofNullable(companyMapper.selectCompanyById(compId));
    }

    /**
     * 기업 회원 정보를 갱신합니다.
     * - @param companyVo 갱신할 기업 회원 정보 객체
     */
    @Override
    public void updateCompany(CompanyVo companyVo) {
        companyMapper.updateCompany(companyVo);
    }

    /**
     * 주어진 회사 ID에 해당하는 기업 회원 정보를 삭제합니다.
     * - @param compId 삭제할 기업 회원의 회사 ID
     */
    @Override
    public void deleteCompany(String compId) {
        companyMapper.deleteCompany(compId);
    }
}