package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author wjh
 * @create 2021 - 07 - 22 10:45
 */

@Configuration //表示该类为配置类，用于导入第三方bean
public class AlphaConfig {

    @Bean //导入第三方bean
    //方法名即为bean名
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    }
}
