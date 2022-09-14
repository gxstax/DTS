package com.ant.dts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * <p>
 * 项目启动入口
 * </p>
 *
 * @author Ant
 * @since 2021/12/31 5:31 下午
 */
@EnableCaching
@SpringBootApplication
public class DataTransferCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataTransferCenterApplication.class, args);
    }

}
