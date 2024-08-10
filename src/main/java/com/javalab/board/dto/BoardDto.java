package com.javalab.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * BoardDto 게시물 데이터 전송 전용 클래스
 * 이는 데이터 전송 객체(Data Transfer Object)로, 클라이언트와 서버 간의
 * 데이터 전송을 위한 객체. DTO는 주로 사용자 인터페이스와 상호작용할 때 사용되며,
 * 입력 검증이나 데이터 형식 변환과 같은 역할을 한다.
 *
 * [보안]
 * BoardDto: 클라이언트와 서버 간 데이터 전송 시 민감한 정보를 포함하지 않도록 설계할 수 있다.
 *           필요한 데이터만 포함함으로써 보안성을 높일 수 있다.
 * BoardVo: 데이터베이스 엔티티이기 때문에 모든 필드를 노출할 가능성이 있으며, 이는 보안 문제를
 *          야기할 수 있다.
 *
 * [직렬화]
 * DTO는 주로 컨트롤러 메서드의 파라미터나 리턴 타입으로 사용되며, 꼭 직렬화가 필요하지 않을 수 있다.
 *
 * [빌더패턴]
 * BoardDto: 빌더 패턴을 사용하여 객체 생성 시, 가독성과 유지보수성을 높일 수 있다.
 * AllArgsConstructor : 모든 필드를 파라미터로 받는 생성자를 만들어준다. 빌더패턴 사용시 필요함.
 *                      빌더패턴 사용시 모든 필드를 파라미터로 받는 생성자를 이용해서 객체를 생성함.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder // 빌더패턴적용
public class BoardDto {

    private int bno;

    // import jakarta.validation.constraints.NotBlank;
    //@NotBlank(message = "제목은 필수 입력입니다.")
    @Size(min = 1, max = 100,  message = "제목은 1자 이상 100자 이내로 입력하세요.")
    private String title;

    //@NotBlank(message = "내용은 필수 입력입니다.")
    @Size(min = 1, max = 1000, message = "내용은 적어도 1자 이상 1000자 이내로 입력하세요.")
    private String content;

    //@NotBlank(message = "작성자는 필수 입력입니다.")
    @Size(min = 5, max = 20, message = "작성자는 5자이상 20자 미만으로 입력하세요.")
    private String memberId;

    private int hitNo;

    @NotNull(message = "작성일자는 필수입력입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date regDate;   // java.util.Date
}
