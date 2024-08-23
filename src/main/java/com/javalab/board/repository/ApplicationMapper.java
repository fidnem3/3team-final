package com.javalab.board.repository;

import com.javalab.board.dto.ApplicationDto;
import com.javalab.board.vo.JobPostVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApplicationMapper {

    void insertApplication(ApplicationDto application);

    List<ApplicationDto> selectApplicationsByJobSeekerId(String jobSeekerId);

    void deleteApplicationById(Long applicationId);

    JobPostVo selectJobPostByApplicationId(@Param("applicationId") Long applicationId);

    ApplicationDto getApplicationById(@Param("applicationId") Long applicationId);


    //알림 기능 시작

    String getCompanyIdByJobPostId(Long jobPostId);

    int countUnreadApplications(String compId);

    void markApplicationAsRead(Long applicationId);

    List<ApplicationDto> selectApplicationsByCompanyId(String compId);


    int countApplications();




}
