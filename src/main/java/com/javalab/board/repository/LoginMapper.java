package com.javalab.board.repository;

import com.javalab.board.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {

	MemberVo login(@Param("memberId") String memberId);

	void save(MemberVo member);

	void saveRole(@Param("memberId") String memberId, @Param("roleName") String roleName);
}
