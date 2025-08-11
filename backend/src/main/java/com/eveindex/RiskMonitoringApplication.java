package com.eveindex;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@MapperScan("com.eveindex.mapper")
@EnableScheduling


public class RiskMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskMonitoringApplication.class, args);
    }
}
