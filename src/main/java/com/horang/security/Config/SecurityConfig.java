package com.horang.security.Config;

import com.horang.security.Service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //인증을 무시하기 위한 설정
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
    }

    /**
     * PasswordEncoder : 입력받은 비밀번호를 그대로 DB에 저장하는 것이 아니고 암호화를 해서 저장을 해주어야 합니다.
     *      따라서 이러한 암호화를 해주는 메서드로 다른 곳에서 사용할 수 있도록 @Bean 으로 등록을 해줍니다.
     * BCryptPasswordEncoder() : password 암호화 방법 중 한 가지입니다.
     * configure(WebSecurity web) : WebSecurity 는 FilterChainProxy 를 생성하는 필터로서 ignoring() 을 사용하여
     *      Spring Security 가 무시할 수 있도록 설정을 할 수 있습니다. 파일의 기준 경로는 resources/static 이라고 합니다.
     * configure(HttpSecurity http) : HttpSecurity 는 Http 로 들어오는 요청에 대하여 보안을 구성할 수 있는 클래스로
     *      authorizeRequests(), formLogin(), logout(), exceptionHandling()과 같은 메서드들을 이용해 로그인에 대한 설정을 해줍니다.
     * configure(AuthenticationManagerBuilder auth) : AuthenticationManagerBuilder 는 Spring Security 의
     *      모든 인증을 관리하는 AuthenticationManager 를 생성하는 클래스로 UserDetailService 를 통해 유저의 정보를 memberService 에서 찾아 담아줍니다.
     *      그리고 passwordEncoder 로는 앞에서 Bean 으로 등록한 passwordEncoder()를 사용하겠다고 설정을 해줍니다.
     */


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").permitAll()

                .and()
                //로그인 설정
                .formLogin()
                .failureUrl("/fail")
                //커스텀 로그인 페이지를 사용
                .loginPage("/member/login")
                //로그인 성공 시 이동할 페이지
                .defaultSuccessUrl("/success")
                .permitAll()

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                .logoutSuccessUrl("/")
                //세션 초기화
                .invalidateHttpSession(true)

                .and()
                .exceptionHandling();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //로그인 처리를 하기 위한 AuthenticationManagerBuilder 를 설정
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}
