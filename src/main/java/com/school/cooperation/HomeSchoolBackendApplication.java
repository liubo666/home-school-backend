package com.school.cooperation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 家校协同教育系统后端主启动类
 *
 * @author Home School Team
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.school.cooperation")
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class HomeSchoolBackendApplication {

    public static void main(String[] args) {
        // 先启动 Spring 应用
        SpringApplication.run(HomeSchoolBackendApplication.class, args);

        // 用换行符 \n 处理多行文本，或使用 Java 文本块（Java 15+ 支持）
        System.out.println("========================================" +
                "\n家校协同教育系统后端启动成功！" +
                "\n========================================" +
                "\n" +
                "\nAPI文档地址: http://localhost:8080/doc.html" +
                "\n健康检查地址: http://localhost:8080/actuator/health" +
                "\n");
    }
}