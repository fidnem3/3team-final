package com.javalab.board.repository;

import com.javalab.board.dto.ApplicationDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    void insertApplication(ApplicationDto application);

    List<ApplicationDto> selectApplicationsByJobSeekerId(String jobSeekerId);

}
