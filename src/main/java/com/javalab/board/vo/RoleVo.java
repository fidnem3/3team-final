package com.javalab.board.vo;

import lombok.Data;

@Data
public class RoleVo {
    private String roleId;     // 역할 ID
    private String roleName;   // 역할 유형 (예: 'ROLE_ADMIN', 'ROLE_COMPANY', 'ROLE_USER')
}
