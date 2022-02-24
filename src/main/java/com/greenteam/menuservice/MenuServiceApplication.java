package com.greenteam.menuservice;

import com.greenteam.menuservice.repository.prodottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;


@EnableMongoRepositories
@SpringBootApplication
public class MenuServiceApplication {

    @Autowired
    prodottoRepository prodottoRepository;
    public static void main(String[] args) {
        SpringApplication.run(MenuServiceApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
