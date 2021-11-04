package com.horang.security.Service;

import com.horang.security.Controller.Domain.Member;
import com.horang.security.Dto.MemberDTO;
import com.horang.security.Repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;

    //회원가입
    @Transactional
    public Long signUp(MemberDTO dto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        //password 를 암호화 한 뒤 db에 저장
        return memberRepository.save(dto.toEntity()).getId();
    }

    /**
     * loadUserByUsername(String username) : Spring Security 가 제공하는
     *      로그인을 사용하기 위해 UserDetailsService 를 구현해주어야 합니다.
     *      로그인 form 에서 입력받은 username 을 가지고 DB를 찾은 뒤 있다면
     *      권한 정보를 추가해주어 UserDetails 라는 객체로 반환을 해줍니다.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //로그인을 하기 위해 가입된 user 정보를 조회하는 메서드
        Optional<Member> memberWrapper = memberRepository.findByUsername(username);
        Member member = memberWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        //여기서는 간단하게 username이 "admin"이면 admin 권한 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }

        //아이디, 비밀번호, 권한리스트를 매개변수로 User 를 만들어 반환해준다.
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
