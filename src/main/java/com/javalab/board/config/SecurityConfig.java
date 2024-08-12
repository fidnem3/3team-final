package com.javalab.board.config;

import com.javalab.board.handler.AuthFailureHandler;
import com.javalab.board.handler.AuthSuccessHandler;
import com.javalab.board.security.CustomOAuth2UserService;
import com.javalab.board.security.handler.CustomAccessDeniedHandler;
import com.javalab.board.security.handler.CustomSocialLoginSuccessHandler;
import com.javalab.board.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;
	private final AuthSuccessHandler authSucessHandler;	// 로그인 성공 후처리를 담당하는 클래스
	private final AuthFailureHandler authFailureHandler;	// 로그인 실패 후처리를 담당하는 클래스

	@Bean
	public PasswordEncoder passwordEncoder() {	// 비밀번호 암호화를 위한 빈
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {	// 권한이 없는 페이지에 접근시 처리를 담당하는 빈
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {	// 소셜 로그인 성공 후처리를 담당하는 빈
		return new CustomSocialLoginSuccessHandler(passwordEncoder());
	}

	/**
	 * [securityFilterChain 메소드]
	 * - HttpSecurity 빈 등록
	 * - HttpSecurity 객체를 이용하여 보안 설정
	 * - @param http : HttpSecurity 객체
	 * - @param customOAuth2UserService : CustomOAuth2UserService 객체
	 * - @return SecurityFilterChain : SecurityFilterChain 객체
	 * - @throws Exception : 예외처리
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {

		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());

		http
				.formLogin(formLogin -> formLogin
						.loginPage("/member/login")	// 로그인 페이지(MembmerController 에서 정의한 경로)
//						.loginProcessingUrl("/member/login") // 로그인 처리 URL, form 태그의 action 경로와 일치해야 함. 그래야 시큐리티가 인식하고 로그인 처리를 시작한다.
						.successHandler(authSucessHandler)
						.failureHandler(authFailureHandler)
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
						.logoutSuccessUrl("/member/login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/scss/**", "/lib/**", "/assets/**").permitAll()
						.requestMatchers("/", "/home", "/about", "/contact").permitAll()  // 필요에 따라 추가
						.requestMatchers("/view/**").permitAll()
						.requestMatchers("/member/**").permitAll()
						//.requestMatchers("/member/modify").hasRole("USER")
						//.requestMatchers("/member/modify").permitAll()
						//.requestMatchers("/board/**").permitAll()
						//.requestMatchers("/item/view/**", "/item/list/**", "/item/read/**").permitAll()
						//.requestMatchers("/item/register/**", "/item/modify/**", "/item/remove/**").hasRole("ADMIN")
						//.requestMatchers("/cart/**", "cartItem/**").hasAnyRole("USER", "ADMIN")
						//.requestMatchers("/order/**", "/orders/**", "orderDetails").hasAnyRole("USER", "ADMIN")
						//.requestMatchers("/admin/**").hasRole("ADMIN")
						//.requestMatchers("/api/track/**").hasAnyRole("USER", "ADMIN")
						.anyRequest().authenticated()
				)
				.sessionManagement(sessionManagement -> sessionManagement
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
						.expiredUrl("/login?error=true&exception=Have been attempted to login from a new place. or session expired")
				)
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler())
				)
//				.csrf(AbstractHttpConfigurer::disable)
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/member/login")
						.successHandler(authenticationSuccessHandler())
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
				);

		http.authenticationManager(auth.build());	// 인증 매니저 설정

		return http.build();	// 설정한 HttpSecurity 객체 반환
	}


}