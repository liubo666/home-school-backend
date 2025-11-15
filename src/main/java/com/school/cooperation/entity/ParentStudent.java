package com.school.cooperation.entity;

import com.school.cooperation.entity.enums.ParentRelation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 家长学生关联关系实体类
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "edu_parent_student",
       uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "student_id"}))
public class ParentStudent {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 家长ID
     */
    @NotNull(message = "家长ID不能为空")
    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    /**
     * 学生ID
     */
    @NotNull(message = "学生ID不能为空")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * 关系类型
     */
    @NotNull(message = "关系类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "relation", nullable = false, length = 20)
    private ParentRelation relation;

    /**
     * 是否为主要联系人
     */
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

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
     * 关联的家长用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private User parent;

    /**
     * 关联的学生信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;
}