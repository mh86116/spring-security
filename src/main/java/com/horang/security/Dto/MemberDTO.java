package com.horang.security.Dto;

import com.horang.security.Controller.Domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String username;
    private String password;

    //Member 객체로 변환
    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }

    @Builder
    public MemberDTO(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }


}
