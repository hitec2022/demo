package com.sparta.demo.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.demo.model.User;
import com.sparta.demo.service.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService){
        System.out.println("UserController Start");
        this.userService = userService;
        System.out.println("UserService ");
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void join(@RequestBody User user) throws Exception{
        System.out.println("회원가입 컨트롤러 실행" + user);
        userService.joinUser(user);
        System.out.println("회원가입 완료");
    }

    @GetMapping("/login")
    public void loginPage() throws Exception{
        System.out.println("login Page");
    }

    @GetMapping("/loginOk")
    public Map<String, String> loginOk() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String authorities = authentication.getAuthorities().toString();

        System.out.println("로그인한 유저 이메일:" + email);
        System.out.println("유저 권한:" + authentication.getAuthorities());

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", email);
        userInfo.put("authorities", authorities);

        return userInfo;
    }

    @GetMapping("/logoutOk")
    public void logoutOk() throws Exception {
        System.out.println("로그아웃 성공");
    }

    @GetMapping("/user")
    public User getUserPage() throws Exception{
        System.out.println("일반 인증 성공");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 유저 정보
        User user = userService.getUserInfo(email);
        return user;
    }
}
