package com.kgc.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableDubbo
@SpringBootApplication
public class GoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }

}
