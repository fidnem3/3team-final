package com.javalab.board.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResumeFileDto {
    private long id;
    private long resumeId;
    private String originalFileName;
    private String storedFileName;

}
