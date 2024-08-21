package com.javalab.board.service;

import com.javalab.board.vo.SuggestionVo;

import java.util.List;

public interface SuggestionService {
    Long saveSuggestion(SuggestionVo suggestionVo);
    List<SuggestionVo> getAllSuggestions();
    void deleteSuggestion(Long sugId);

}
