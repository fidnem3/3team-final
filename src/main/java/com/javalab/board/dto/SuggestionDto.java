package com.javalab.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javalab.board.vo.JobSeekerVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class SuggestionDto {
        private Long sugId;
        private String title;
        private String content;
        private String name;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date created;
        private String tel;
}

