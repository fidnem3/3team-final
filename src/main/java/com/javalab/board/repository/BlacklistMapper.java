package com.javalab.board.repository;

import com.javalab.board.dto.BlacklistDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlacklistMapper {

    int addBlacklist(BlacklistDto blacklist);

    void updateBlacklist(BlacklistDto blacklist);

    BlacklistDto getBlacklistStatus(@Param("id") String id, @Param("type") String type);

    List<BlacklistDto> getAllBlacklist();

    List<BlacklistDto> getActiveBlacklist();

    void deleteBlacklist(@Param("jobSeekerId") String jobSeekerId, @Param("compId") String compId);
}