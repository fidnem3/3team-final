package com.javalab.board.repository;

import com.javalab.board.dto.ResumeDto;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ResumeMapper {

    private final SqlSessionTemplate sql;

    public void save(ResumeDto resumeDto) {
        sql.insert("com.javalab.board.repository.ResumeMapper.save" , resumeDto);
    }
}
