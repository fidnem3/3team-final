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
    private Date blacklistDate;
    private String type;
    private boolean isBlacklisted;
}
