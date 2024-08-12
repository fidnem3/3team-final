//
//
//package com.javalab.board.service;
//
//import com.javalab.board.dto.MemberDto;
//import com.javalab.board.repository.LoginMapper;
//import com.javalab.board.vo.MemberVo;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class MemberService implements UserDetailsService {
//
//    private final LoginMapper loginMapper;
//
//    // 실제 인증 진행(DB에 이메일로 사용자 정보 조회해옴)
//    @Override
//    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
//
//        MemberVo member = loginMapper.login(memberId);
//
//        if (member == null) {
//            throw new UsernameNotFoundException(memberId);
//        }
//
//        List<GrantedAuthority> authorities = member.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role))
//                .collect(Collectors.toList());
//
//        return new MemberDto(
//                member.getMemberId(),
//                member.getPassword(),
//                authorities,
//                member.getName(),
//                member.getEmail(),
//                member.getPoint(),
//                member.isDel(),
//                member.isSocial()
//        );
//    }
//
//
//    /**
//     * 카카오 소셜 로그인 사용자 비밀번호 변경.
//     * 카카오 소셜 로그인 사용자의 social 컬럼값을 0(false)로 변경
//     * 즉, 일반사용자로 전환되서 아이디/비밀번호로 로그인 가능.
//     */
////    @Transactional
////    public void modifyPasswordAndSocialStatus(String email, String encodedPassword) {
////
////        // 비밀번호 변경
////        memberRepository.updatePasswordAndSocial(encodedPassword, email);
////    }
//
////    public void saveMember(MemberVo member) {
////        loginMapper.save(member);
////        loginMapper.saveRole(member.getMemberId(), "ROLE_USER");
////    }
//
//}
