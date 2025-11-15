package com.school.cooperation.entity;

import com.school.cooperation.entity.enums.Importance;
import com.school.cooperation.entity.enums.RecordType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 学生档案记录实体类
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "edu_student_record")
public class StudentRecord extends BaseEntity {

    /**
     * 学生ID
     */
    @NotNull(message = "学生ID不能为空")
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * 教师ID
     */
    @NotNull(message = "教师ID不能为空")
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    /**
     * 记录类型
     */
    @NotNull(message = "记录类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private RecordType type;

    /**
     * 记录分类
     */
    @NotBlank(message = "记录分类不能为空")
    @Size(max = 50, message = "记录分类长度不能超过50个字符")
    @Column(name = "category", nullable = false, length = 50)
    private String category;

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
     * 图片URL列表 (JSON格式)
     */
    @Column(name = "images", columnDefinition = "JSON")
    private String images;

    /**
     * 标签列表 (JSON格式)
     */
    @Column(name = "tags", columnDefinition = "JSON")
    private String tags;

    /**
     * 重要程度
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "importance", nullable = false, length = 20)
    private Importance importance = Importance.MEDIUM;

    /**
     * 是否对家长公开
     */
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    /**
     * 是否已通知家长
     */
    @Column(name = "parent_notified", nullable = false)
    private Boolean parentNotified = false;

    /**
     * 关联的学生信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    /**
     * 关联的教师信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private User teacher;

    /**
     * 获取图片URL列表
     */
    @Transient
    public List<String> getImageList() {
        // TODO: 实现JSON解析逻辑
        return null;
    }

    /**
     * 设置图片URL列表
     */
    @Transient
    public void setImageList(List<String> imageList) {
        // TODO: 实现JSON序列化逻辑
    }

    /**
     * 获取标签列表
     */
    @Transient
    public List<String> getTagList() {
        // TODO: 实现JSON解析逻辑
        return null;
    }

    /**
     * 设置标签列表
     */
    @Transient
    public void setTagList(List<String> tagList) {
        // TODO: 实现JSON序列化逻辑
    }

    /**
     * 是否为表扬记录
     */
    @Transient
    public boolean isPraise() {
        return RecordType.PRAISE.equals(type);
    }

    /**
     * 是否为批评记录
     */
    @Transient
    public boolean isCriticism() {
        return RecordType.CRITICISM.equals(type);
    }

    /**
     * 是否为中性记录
     */
    @Transient
    public boolean isNeutral() {
        return RecordType.NEUTRAL.equals(type);
    }

    /**
     * 是否为高重要程度
     */
    @Transient
    public boolean isHighImportance() {
        return Importance.HIGH.equals(importance);
    }
}