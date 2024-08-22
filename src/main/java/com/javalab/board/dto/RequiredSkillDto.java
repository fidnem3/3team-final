package com.javalab.board.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequiredSkillDto {
    private Long requiredSkillId;
    private Long jobPostId;
    private String skill;

}
