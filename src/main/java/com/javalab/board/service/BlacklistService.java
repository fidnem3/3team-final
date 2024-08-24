package com.javalab.board.service;

import com.javalab.board.dto.BlacklistDto;
import com.javalab.board.repository.BlacklistMapper;
import com.javalab.board.repository.CompanyMapper;
import com.javalab.board.vo.CompanyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BlacklistService {
    private static final Logger logger = LoggerFactory.getLogger(BlacklistService.class);

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private CompanyMapper companyMapper;

    public boolean toggleBlacklist(String compId, String type, String reason) {
        // 블랙리스트 상태를 조회하여 Integer 값으로 받아옴
        Integer isBlacklistedValue = blacklistMapper.getIsBlacklisted(compId);

        // null 체크를 포함하여 Boolean 값으로 변환
        boolean isCurrentlyBlacklisted = (isBlacklistedValue != null && isBlacklistedValue == 1);

        // 블랙리스트 상태 토글 (0 -> 1 또는 1 -> 0)
        int newStatus = isCurrentlyBlacklisted ? 0 : 1;
        blacklistMapper.updateBlacklistStatus(compId, newStatus, reason);

        // 새로운 상태 반환
        return newStatus == 1;
    }


//    public boolean toggleBlacklist(String id, String type, String reason) {
//        // 블랙리스트 상태를 토글 (0 -> 1, 1 -> 0)
//
//        boolean isCurrentlyBlacklisted = blacklistMapper.getIsBlacklisted(id) == 1;
//
//        // 상태를 반전시켜 업데이트
//        int newStatus = isCurrentlyBlacklisted ? 0 : 1;
//        blacklistMapper.updateBlacklistStatus(id, newStatus);
//
//        // 회사 정보를 가져와 업데이트 (필요한 경우)
//        CompanyVo company = companyMapper.getCompanyById(id);
//        if (company != null) {
//            company.setBlacklisted(newStatus == 1);
//            companyMapper.updateCompany(company);  // 이 부분은 필요에 따라 처리
//        }
//
//        return newStatus == 1;
//    }

//    public boolean toggleBlacklist(String id, String type, String reason) {
//        try {
//            logger.info("Toggling blacklist for id: {}, type: {}, reason: {}", id, type, reason);
//            BlacklistDto existingBlacklist = blacklistMapper.getBlacklistStatus(id, type);
//            if (existingBlacklist == null) {
//                BlacklistDto newBlacklist = new BlacklistDto();
//                newBlacklist.setType(type);
//                if ("company".equals(type)) {
//                    newBlacklist.setCompId(id);
//                } else {
//                    newBlacklist.setJobSeekerId(id);
//                }
//                newBlacklist.setIsBlacklisted(1);  // 1은 true를 의미
//                newBlacklist.setBlacklistDate(new Date());
//                newBlacklist.setReason(reason != null ? reason : "");
//                blacklistMapper.addBlacklist(newBlacklist);
//                logger.info("Added new blacklist entry for id: {}", id);
//                return true;
//            } else {
//                int newBlacklistStatus = (existingBlacklist.getIsBlacklisted() == 1) ? 0 : 1;
//                existingBlacklist.setIsBlacklisted(newBlacklistStatus);
//                existingBlacklist.setReason(reason != null ? reason : existingBlacklist.getReason());
//                blacklistMapper.updateBlacklist(existingBlacklist);
//                logger.info("Updated existing blacklist entry for id: {}. New status: {}", id, newBlacklistStatus);
//                return newBlacklistStatus == 1;
//            }
//        } catch (Exception e) {
//            logger.error("Error in toggleBlacklist", e);
//            throw new RuntimeException("Failed to toggle blacklist status", e);
//        }
//    }

    @Transactional(readOnly = true)
    public boolean isBlacklisted(String id, String type) {
        try {
            BlacklistDto blacklist = blacklistMapper.getBlacklistStatus(id, type);
            return blacklist != null && blacklist.isBlacklisted();
        } catch (Exception e) {
            logger.error("Error in isBlacklisted", e);
            throw new RuntimeException("Failed to check blacklist status", e);
        }
    }

    @Transactional(readOnly = true)
    public List<BlacklistDto> getAllBlacklist() {
        try {
            return blacklistMapper.getAllBlacklist();
        } catch (Exception e) {
            logger.error("Error in getAllBlacklist", e);
            throw new RuntimeException("Failed to get all blacklist entries", e);
        }
    }

    @Transactional(readOnly = true)
    public List<BlacklistDto> getActiveBlacklist() {
        try {
            return blacklistMapper.getActiveBlacklist();
        } catch (Exception e) {
            logger.error("Error in getActiveBlacklist", e);
            throw new RuntimeException("Failed to get active blacklist entries", e);
        }
    }

    public void updateBlacklistReason(String id, String type, String reason) {
        try {
            logger.info("Updating blacklist reason for id: {}, type: {}", id, type);
            BlacklistDto blacklist = blacklistMapper.getBlacklistStatus(id, type);
            if (blacklist != null) {
                blacklist.setReason(reason);
                blacklistMapper.updateBlacklist(blacklist);
                logger.info("Updated blacklist reason for id: {}", id);
            } else {
                logger.warn("No blacklist entry found for id: {} and type: {}", id, type);
            }
        } catch (Exception e) {
            logger.error("Error in updateBlacklistReason", e);
            throw new RuntimeException("Failed to update blacklist reason", e);
        }
    }

    @Transactional
    public void deleteBlacklist(String jobSeekerId, String compId) {
        blacklistMapper.deleteBlacklist(jobSeekerId, compId);
    }
}