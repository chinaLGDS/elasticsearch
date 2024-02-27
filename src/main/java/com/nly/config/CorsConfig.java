package com.nly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    //設置跨域
    @Bean
    public CorsFilter corsFilter(){
        //1.添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        //添加允许的跨域信息的内容。也就是前端。相当于添加请求的调用端
        //不建议使用*。会导致不管是哪里都可以像我们的服务其进行请求。会有安全隐患。
        config.addAllowedOrigin("http://localhost:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://center.z.mukewang.com:8080");
//        config.addAllowedOrigin("http://shop.z.mukewang.com");
//        config.addAllowedOrigin("http://center.z.mukewang.com");
//        服务器配置
        config.addAllowedOrigin("http://118.195.254.199:8080");
        config.addAllowedOrigin("http://118.195.254.199:90");
        config.addAllowedOrigin("http://118.195.254.199");
//        虚拟机配置1
//        config.addAllowedOrigin("http://192.168.2.10:8080");
//        config.addAllowedOrigin("http://192.168.2.10:90");
//        config.addAllowedOrigin("http://192.168.2.10");
//        config.addAllowedOrigin("http://192.168.56.102:8080");
//        config.addAllowedOrigin("http://192.168.56.102:90");
//        config.addAllowedOrigin("http://192.168.56.102");



//        http://118.195.254.199:8080
        config.addAllowedOrigin("*");

        //凭证。是否可以让我们请求去携带一些相应的内容。比如说：是否允许发送cookie信息
        //设置是否允许发送cookie信息
        config.setAllowCredentials(true);
        //是否要放行那些请求的方式。get,put,post
        //设置允许请求的方式
        config.addAllowedMethod("*");

        //设置允许的的header
        config.addAllowedHeader("*");

        //2.为url添加路径映射
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        //请求适用于所有的路由。
        corsSource.registerCorsConfiguration("/**",config );

        //3.返回重新定义好的corsSource
        return new CorsFilter(corsSource);

    }
}
