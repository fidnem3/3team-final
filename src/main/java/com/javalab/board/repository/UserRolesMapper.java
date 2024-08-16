package com.javalab.board.repository;

import com.javalab.board.vo.UserRolesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRolesMapper {

    //@Insert("INSERT INTO UserRoles (user_id, user_type, role_id) VALUES (#{userId}, #{userType}, #{roleId})")
    void insertUserRole(UserRolesVo userRolesVo);

    //@Select("SELECT * FROM UserRoles WHERE user_id = #{userId} AND user_type = #{userType}")
    List<UserRolesVo> selectUserRole(String userId, String userType);

    //@Delete("DELETE FROM UserRoles WHERE user_id = #{userId} AND user_type = #{userType} AND role_id = #{roleId}")
    void deleteUserRole(String userId, String userType, String roleId);
}
