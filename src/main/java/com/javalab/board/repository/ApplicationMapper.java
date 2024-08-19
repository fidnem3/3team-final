package com.javalab.board.repository;

import com.javalab.board.dto.ApplicationDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper {

    void insertApplication(ApplicationDto application);
}
