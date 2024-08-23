package com.javalab.board.dto;

import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BlacklistDto {

    private String jobSeekerId;
    private Long blackId;
    private String compId;
    private String reason;
    private Date blacklistDate;  // 기존 Date 타입 유지
    private String type;

    @Builder.Default
    private Integer isBlacklisted = 0;  // boolean 대신 Integer 사용

    // 편의 메소드 추가
    public boolean isBlacklisted() {
        return isBlacklisted != null && isBlacklisted == 1;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.isBlacklisted = blacklisted ? 1 : 0;
    }

    // type 필드에 대한 유효성 검사
    public void setType(String type) {
        if (!"jobSeeker".equals(type) && !"company".equals(type)) {
            throw new IllegalArgumentException("Type must be either 'jobSeeker' or 'company'");
        }
        this.type = type;
    }

    // isBlacklisted 필드에 대한 유효성 검사
    public void setIsBlacklisted(Integer isBlacklisted) {
        if (isBlacklisted != null && (isBlacklisted != 0 && isBlacklisted != 1)) {
            throw new IllegalArgumentException("isBlacklisted must be either 0 or 1");
        }
        this.isBlacklisted = isBlacklisted;
    }

    private String userId;  // jobSeekerId 또는 compId를 저장
}