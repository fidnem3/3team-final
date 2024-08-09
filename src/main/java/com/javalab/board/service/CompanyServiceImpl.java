package com.javalab.board.service;

import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.vo.CompanyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 새로운 기업 회원을 등록합니다.
     *  - @param companyVo 등록할 기업 회원 정보 객체
     */
    @Override
    public void registerCompany(CompanyVo companyVo) {
        companyMapper.insertCompany(companyVo);
    }

    /**
     * 주어진 ID에 해당하는 기업 회원의 상세 정보를 조회합니다.
     * - @param id 조회할 기업 회원의 ID
     * - @return 조회된 기업 회원 정보 객체를 포함하는 Optional 객체
     */
    @Override
    public Optional<CompanyVo> getCompanyDetails(Long id) {
        return Optional.ofNullable(companyMapper.selectCompanyById(id));
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
     * 주어진 ID에 해당하는 기업 회원 정보를 삭제합니다.
     * - @param id 삭제할 기업 회원의 ID
     */
    @Override
    public void deleteCompany(Long id) {
        companyMapper.deleteCompany(id);
    }
}
