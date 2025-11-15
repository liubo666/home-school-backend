package com.school.cooperation.entity;

import com.school.cooperation.entity.enums.ClassStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 班级实体类
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "edu_class")
public class EduClass extends BaseEntity {

    /**
     * 班级名称
     */
    @NotBlank(message = "班级名称不能为空")
    @Size(max = 50, message = "班级名称长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 年级
     */
    @NotBlank(message = "年级不能为空")
    @Size(max = 20, message = "年级长度不能超过20个字符")
    @Column(name = "grade", nullable = false, length = 20)
    private String grade;

    /**
     * 班主任ID
     */
    @NotNull(message = "班主任不能为空")
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    /**
     * 学生人数
     */
    @Column(name = "student_count", nullable = false)
    private Integer studentCount = 0;

    /**
     * 班级描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 班级状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ClassStatus status = ClassStatus.ACTIVE;

    /**
     * 关联的班主任用户信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private User teacher;
}