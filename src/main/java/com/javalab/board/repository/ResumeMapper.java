package com.javalab.board.repository;

import com.javalab.board.dto.ResumeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//@Repository
@Mapper
public interface ResumeMapper {

    public int createResume(ResumeDto resumeDto);

    public List<ResumeDto> findAll();

//    public void updateHits(int resumeId);

    public ResumeDto findById(int resumeId);

    public void updateResume(ResumeDto resumeDto);

    public void deleteResume(int resumeId);

}


