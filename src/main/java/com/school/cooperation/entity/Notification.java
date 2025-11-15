package com.school.cooperation.entity;

import com.school.cooperation.entity.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 通知消息实体类
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "sys_notification")
public class Notification {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * 通知类型
     */
    @NotNull(message = "通知类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private NotificationType type;

    /**
     * 关联ID (记录/班级/学生ID)
     */
    @Column(name = "related_id")
    private Long relatedId;

    /**
     * 是否已读
     */
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    /**
     * 阅读时间
     */
    @Column(name = "read_time")
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * JPA生命周期回调 - 创建前设置时间
     */
    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }

    /**
     * 关联的用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    /**
     * 标记为已读
     */
    public void markAsRead() {
        this.isRead = true;
        this.readTime = LocalDateTime.now();
    }

    /**
     * 是否为系统通知
     */
    @Transient
    public boolean isSystemNotification() {
        return NotificationType.SYSTEM.equals(type);
    }

    /**
     * 是否为记录通知
     */
    @Transient
    public boolean isRecordNotification() {
        return NotificationType.RECORD.equals(type);
    }

    /**
     * 是否为班级通知
     */
    @Transient
    public boolean isClassNotification() {
        return NotificationType.CLASS.equals(type);
    }

    /**
     * 是否为学生通知
     */
    @Transient
    public boolean isStudentNotification() {
        return NotificationType.STUDENT.equals(type);
    }
}