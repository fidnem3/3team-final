package com.javalab.board.repository;

import com.javalab.board.dto.ResumeDto;
import com.javalab.board.vo.BoardVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
@Mapper
public interface ResumeMapper {

    public int createResume(ResumeDto resumeDto);

    public List<ResumeDto> findAll();
}

//    private final SqlSessionTemplate sql;
//
//    public void save(ResumeDto resumeDto) {
//        sql.insert("com.javalab.board.repository.ResumeMapper.save" , resumeDto);

