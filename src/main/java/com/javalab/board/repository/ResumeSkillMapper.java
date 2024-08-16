package com.javalab.board.repository;

import com.javalab.board.dto.ResumeSkillDto;
import org.apache.ibatis.annotations.Mapper;

//@Repository
//@RequiredArgsConstructor
@Mapper
public interface ResumeSkillMapper {

     public int resumeSkillCreate(ResumeSkillDto resumeSkillDto);


//        private final SqlSessionTemplate sql;
//
//        public void save(ResumeSkillDto resumeSkillDto){
//            sql.insert("com.javalab.board.repository.ResumeSkillMapper.save" , resumeSkillDto);
    }
