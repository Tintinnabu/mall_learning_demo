package com.taoshi.springboot_mybatis.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */

@Configuration
@MapperScan("com.taoshi.springboot_mybatis.mbg.mapper")
public class MybatisConfig {
}
