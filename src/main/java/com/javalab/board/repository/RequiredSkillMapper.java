package com.javalab.board.repository;

import com.javalab.board.dto.RequiredSkillDto;
import com.javalab.board.dto.ResumeSkillDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RequiredSkillMapper {

    public Long requiredSkillCreate(RequiredSkillDto requiredSkillDto);

    List<String> getRequiredSkillsByJobPostId(Long jobPostId);

}
