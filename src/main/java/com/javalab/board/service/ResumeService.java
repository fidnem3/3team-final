package com.javalab.board.service;

import com.javalab.board.dto.BoardDto;
import com.javalab.board.dto.ResumeDto;
import com.javalab.board.repository.ResumeMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeMapper resumeMapper;


    public void save(ResumeDto resumeDto) {
        resumeMapper.save(resumeDto);
    }
}
