package com.horang.security.Controller;

import com.horang.security.Dto.MemberDTO;
import com.horang.security.Service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    @GetMapping("/member/signup")
    public String signupForm(Model model) {
        model.addAttribute("member", new MemberDTO());

        return "member/signupForm";
    }

    @PostMapping("/member/signup")
    public String signup(MemberDTO dto) {
        memberService.signUp(dto);

        return "redirect:/";
    }

    @GetMapping("/member/login")
    public String login() {
        return "member/loginForm";
    }

    @GetMapping("/success")
    public String success() { return "common/success"; }

    @GetMapping("/fail")
    public String fail() { return "common/fail"; }
}
