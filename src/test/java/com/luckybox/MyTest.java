package com.luckybox;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
public class MyTest {
//    @Resource
//    private IAuthService authService;
//    @Resource
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    @Test
//    void createAdmin() {
//        User user = new User();
//        user.setNickname("admin");
//        user.setPhone("15789788978");
//        user.setRole("ADMIN");
//        user.setPassword(bCryptPasswordEncoder.encode("wyh1234"));
//        authService.save(user);
//    }
    @Test
    void cs() {
        //key时间戳

        LocalDateTime n = LocalDateTime.now();
        String format = n.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        String f = String.valueOf(n.getNano());
        String ff = format + ":" + f;
        System.out.println(format);
        System.out.println(ff);
    }
}
