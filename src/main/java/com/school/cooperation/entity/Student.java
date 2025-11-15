package com.school.cooperation.entity;

import com.school.cooperation.entity.enums.Gender;
import com.school.cooperation.entity.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 学生实体类
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "edu_student")
public class Student extends BaseEntity {

    /**
     * 学号
     */
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Column(name = "student_id", nullable = false, unique = true, length = 20)
    private String studentId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 性别
     */
    @NotNull(message = "性别不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @Past(message = "出生日期必须是过去的日期")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号必须为18位")
    @Column(name = "id_card", nullable = false, unique = true, length = 18)
    private String idCard;

    /**
     * 班级ID
     */
    @NotNull(message = "班级不能为空")
    @Column(name = "class_id", nullable = false)
    private Long classId;

    /**
     * 家庭住址
     */
    @Size(max = 255, message = "家庭住址长度不能超过255个字符")
    @Column(name = "address", length = 255)
    private String address;

    /**
     * 紧急联系人姓名
     */
    @Size(max = 50, message = "紧急联系人姓名长度不能超过50个字符")
    @Column(name = "emergency_contact_name", length = 50)
    private String emergencyContactName;

    /**
     * 紧急联系人电话
     */
    @Size(max = 20, message = "紧急联系人电话长度不能超过20个字符")
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    /**
     * 紧急联系人关系
     */
    @Size(max = 20, message = "紧急联系人关系长度不能超过20个字符")
    @Column(name = "emergency_contact_relation", length = 20)
    private String emergencyContactRelation;

    /**
     * 入学日期
     */
    @NotNull(message = "入学日期不能为空")
    @Past(message = "入学日期必须是过去的日期")
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    /**
     * 学生状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StudentStatus status = StudentStatus.ACTIVE;

    /**
     * 关联的班级信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", insertable = false, updatable = false)
    private EduClass eduClass;

    /**
     * 学生是否在读
     */
    @Transient
    public boolean isActive() {
        return StudentStatus.ACTIVE.equals(status);
    }

    /**
     * 学生是否已毕业
     */
    @Transient
    public boolean isGraduated() {
        return StudentStatus.GRADUATED.equals(status);
    }

    /**
     * 学生是否已转出
     */
    @Transient
    public boolean isTransferred() {
        return StudentStatus.TRANSFERRED.equals(status);
    }
}