package com.javalab.board.repository;
import com.javalab.board.dto.ResumeSkillDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//@Repository
//@RequiredArgsConstructor
public interface RequiredSkillMapper {

    public int resumeSkillCreate(ResumeSkillDto resumeSkillDto);

    List<String> getRequiredSkillsByJobPostId(Long jobPostId);


//        private final SqlSessionTemplate sql;
//
//        public void save(ResumeSkillDto resumeSkillDto){
//            sql.insert("com.javalab.board.repository.ResumeSkillMapper.save" , resumeSkillDto);
}
