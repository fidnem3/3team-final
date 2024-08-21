package com.javalab.board.service;

import com.javalab.board.dto.CreateJobPostRequestDto;
import com.javalab.board.dto.SuggestionDto;
import com.javalab.board.repository.*;
import com.javalab.board.vo.CompanyVo;
import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.SuggestionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private SuggestionMapper suggestionMapper;
    @Autowired
    private JobSeekerMapper jobSeekerMapper;

    @Override
    @Transactional
    public Long saveSuggestion(SuggestionVo suggestionVo) {
        suggestionMapper.insertSuggestion(suggestionVo);
        return suggestionVo.getSugId(); // 데이터베이스에서 생성된 ID를 반환
    }


    private SuggestionVo SuggestionDtoToSuggestionVo(SuggestionDto dto) {
        return SuggestionVo.builder()
                .name(dto.getName())
                .tel(dto.getTel())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }


    @Override
    public List<SuggestionVo> getAllSuggestions() {
        return suggestionMapper.getAllSuggestions();
    }


    @Override
    public void deleteSuggestion(Long sugId) {suggestionMapper.deleteSuggestion(sugId);
    }


}

