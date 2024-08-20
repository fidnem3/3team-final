package com.javalab.board.repository;

import com.javalab.board.dto.ApplicationDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    void insertApplication(ApplicationDto application);

    List<ApplicationDto> selectApplicationsByJobSeekerId(String jobSeekerId);

    void deleteApplicationById(Long applicationId);

    //알림 기능 시작

    String getCompanyIdByJobPostId(Long jobPostId);

    int countUnreadApplications(String compId);

    void markApplicationAsRead(Long applicationId);

    List<ApplicationDto> selectApplicationsByCompanyId(String compId);

}
