package com.javalab.board.repository;

import com.javalab.board.vo.JobSeekerScrapVo;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobSeekerScrapMapper {


    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END FROM jobSeekerScrap WHERE jobSeeker_id = #{jobSeekerId} AND jobPost_id = #{jobPostId}")
    boolean existsByJobSeekerIdAndJobPostId(@Param("jobSeekerId") String jobSeekerId, @Param("jobPostId") Long jobPostId);

    List<JobSeekerScrapVo> getScrapListByJobSeekerId(String jobSeekerId);

    void insertScrap(JobSeekerScrapVo scrap);

    void deleteScrap(@Param("scrapId") Long scrapId);

    List<JobSeekerScrapVo> getScrapList(@Param("jobSeekerId") String jobSeekerId);

    @Delete("DELETE FROM jobSeekerScrap WHERE jobSeeker_id = #{jobSeekerId} AND jobPost_id = #{jobPostId}")
    void deleteScrapByJobSeekerIdAndJobPostId(@Param("jobSeekerId") String jobSeekerId, @Param("jobPostId") Long jobPostId);

    public List<JobSeekerScrapVo> listJobSeekerScrap();

    void deleteScrapsByJobPostId(Long jobPostId);

    void deleteScrapsByJobSeekerId(String jobSeekerId);
}
