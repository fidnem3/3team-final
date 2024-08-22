package com.javalab.board.repository;

import com.javalab.board.vo.JobPostVo;
import com.javalab.board.vo.SuggestionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuggestionMapper {
    Long insertSuggestion(SuggestionVo suggestionVo);

    List<SuggestionVo> getAllSuggestions();

    void deleteSuggestion(@Param("sugId") Long sugId);





}
