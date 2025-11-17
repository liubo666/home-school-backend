package com.school.cooperation.config;

import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import com.school.cooperation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化器
 * 在应用启动时创建初始数据
 *
 * @author Home School Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("开始初始化数据...");

        // 创建测试用户
        createTestUsers();

        log.info("数据初始化完成");
    }

    /**
     * 创建测试用户
     */
    private void createTestUsers() {
        if (userRepository.count() > 0) {
            log.info("数据库中已存在用户，跳过初始化");
            return;
        }

        // 超级管理员
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("123456"))
                .realName("超级管理员")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .phone("13800138001")
                .email("admin@example.com")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .deleted(false)
                .build();

        // 学校管理员
        User schoolAdmin = User.builder()
                .username("school_admin")
                .password(passwordEncoder.encode("123456"))
                .realName("学校管理员")
                .role(UserRole.SCHOOL_ADMIN)
                .status(UserStatus.ACTIVE)
                .phone("13800138002")
                .email("school_admin@example.com")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .deleted(false)
                .build();

        // 班主任
        User teacher = User.builder()
                .username("teacher")
                .password(passwordEncoder.encode("123456"))
                .realName("班主任老师")
                .role(UserRole.TEACHER)
                .status(UserStatus.ACTIVE)
                .phone("13800138003")
                .email("teacher@example.com")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .deleted(false)
                .build();

        // 家长
        User parent = User.builder()
                .username("parent")
                .password(passwordEncoder.encode("123456"))
                .realName("学生家长")
                .role(UserRole.PARENT)
                .status(UserStatus.ACTIVE)
                .phone("13800138004")
                .email("parent@example.com")
                .createdTime(LocalDateTime.now())
                .updatedTime(LocalDateTime.now())
                .deleted(false)
                .build();

        // 保存用户
        userRepository.save(admin);
        userRepository.save(schoolAdmin);
        userRepository.save(teacher);
        userRepository.save(parent);

        log.info("创建了4个测试用户：admin, school_admin, teacher, parent");
    }
}