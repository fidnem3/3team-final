package com.javalab.board.repository;

import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.UserRolesVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobSeekerMapper {

    /**
     * 개인 회원 정보를 데이터베이스에 삽입합니다.
     * - @param jobSeekerVo 삽입할 개인 회원 정보 객체
     */
    void insertJobSeeker(JobSeekerVo jobSeekerVo);

    /**
     * 개인 회원의 권한 정보를 데이터베이스에 삽입합니다.
     * - @param userRolesVo 개인 회원의 권한 정보를 포함하는 객체
     */
    void insertJobSeekerRole(UserRolesVo userRolesVo);

    /**
     * 주어진 ID에 해당하는 개인 회원 정보를 조회합니다.
     * - @param jobSeekerId 조회할 개인 회원의 ID
     * - @return 조회된 개인 회원 정보 객체
     */
    JobSeekerVo selectJobSeekerById(String jobSeekerId);

    /**
     * 개인 회원 정보를 데이터베이스에서 갱신합니다.
     * - @param jobSeekerVo 갱신할 개인 회원 정보 객체
     */
    void updateJobSeeker(JobSeekerVo jobSeekerVo);

    /**
     * 주어진 ID에 해당하는 개인 회원 정보를 데이터베이스에서 삭제합니다.
     * - @param jobSeekerId 삭제할 개인 회원의 ID
     */
    void deleteJobSeeker(String jobSeekerId);



}
