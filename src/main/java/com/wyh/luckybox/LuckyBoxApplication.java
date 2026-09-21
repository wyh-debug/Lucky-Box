package com.wyh.luckybox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class LuckyBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuckyBoxApplication.class, args);
	}

}
