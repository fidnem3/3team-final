package com.javalab.board.service;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.repository.BlacklistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlacklistService {
    @Autowired
    private BlacklistMapper blacklistMapper;

    public boolean toggleBlacklist(String id, String type, String reason) {
        BlacklistDto existingBlacklist = blacklistMapper.getBlacklistStatus(id, type);
        if (existingBlacklist == null) {
            BlacklistDto newBlacklist = new BlacklistDto();
            newBlacklist.setType(type);
            if ("company".equals(type)) {
                newBlacklist.setCompId(id);
                newBlacklist.setJobSeekerId(null);
            } else {
                newBlacklist.setJobSeekerId(id);
                newBlacklist.setCompId(null);
            }
            newBlacklist.setBlacklisted(true);
            newBlacklist.setBlacklistDate(new Date());
            newBlacklist.setReason(reason != null ? reason : "");
            blacklistMapper.addBlacklist(newBlacklist);
            return true;
        } else {
            existingBlacklist.setBlacklisted(!existingBlacklist.isBlacklisted());
            existingBlacklist.setReason(reason != null ? reason : "");
            blacklistMapper.updateBlacklist(existingBlacklist);
            return existingBlacklist.isBlacklisted();
        }
    }

    public boolean isBlacklisted(String id, String type) {
        BlacklistDto blacklist = blacklistMapper.getBlacklistStatus(id, type);
        return blacklist != null && blacklist.isBlacklisted();
    }

    public List<BlacklistDto> getAllBlacklist() {
        return blacklistMapper.getAllBlacklist();
    }

    public List<BlacklistDto> getActiveBlacklist() {
        return blacklistMapper.getActiveBlacklist();
    }

    public void updateBlacklistReason(String id, String type, String reason) {
        BlacklistDto blacklist = blacklistMapper.getBlacklistStatus(id, type);
        if (blacklist != null) {
            blacklist.setReason(reason);
            blacklistMapper.updateBlacklist(blacklist);
        }
    }
}