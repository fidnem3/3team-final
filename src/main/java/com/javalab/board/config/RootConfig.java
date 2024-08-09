package com.javalab.board.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Configuration : 이 클래스가 스프링 앱의 환경설정 정보를 담고 있는
 *  클래스임을 알린다. 그리고 앱 구동시 @Bean으로 설정되어 있는 메소드의
 *  반환값인 ModelMapper 객체가 스프링 빈으로 생성되어 스프링 컨테이너에
 *  올라간다.
 */
@Configuration
public class RootConfig {
    /*
      @Bean : getMapper 메소드의 반환 타입인 ModelMapper를
       스프링 Bean 생성한다. 그 빈의 이름은 "modelMapper"이다.
     */
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        return modelMapper;
    }
}
