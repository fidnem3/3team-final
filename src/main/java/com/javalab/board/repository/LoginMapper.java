package com.javalab.board.repository;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.CompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {

	/**
	 * JobSeeker 로그인
	 * - @param jobSeekerId 로그인할 개인회원 ID
	 * - @return 로그인한 개인회원 정보
	 */
	JobSeekerVo loginJobSeeker(@Param("jobSeekerId") String jobSeekerId);

	/**
	 * Company 로그인
	 * - @param compId 로그인할 기업회원 ID
	 * - @return 로그인한 기업회원 정보
	 */
	CompanyVo loginCompany(@Param("compId") String compId);

	/**
	 * JobSeeker 저장
	 * - @param jobSeeker 개인회원 정보 객체
	 */
	void saveJobSeeker(JobSeekerVo jobSeeker);

	/**
	 * Company 저장
	 * - @param company 기업회원 정보 객체
	 */
	void saveCompany(CompanyVo company);

	/**
	 * 사용자 권한 저장
	 * - @param userId 사용자 ID (개인 또는 기업)
	 * - @param roleName 사용자 권한 이름
	 */
	void saveRole(@Param("userId") String userId, @Param("roleName") String roleName);

	/**
	 * 사용자 권한 삭제
	 * - @param userId 사용자 ID (개인 또는 기업)
	 * - @param roleName 사용자 권한 이름
	 */
	void deleteRole(@Param("userId") String userId, @Param("roleName") String roleName);

	/**
	 * 사용자 권한 조회
	 * - @param userId 사용자 ID (개인 또는 기업)
	 * - @return 사용자 권한 목록
	 */
	List<String> getRolesByUserId(@Param("userId") String userId);


	/**
	 * 블랙리스트 정보 조회
	 * */
	BlacklistDto getBlacklistInfo(String userId, String userType);
}
