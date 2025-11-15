package com.school.cooperation.dto;

import com.school.cooperation.entity.enums.ClassStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 班级创建请求DTO
 *
 * @author homeschool
 * @since 1.0.0
 */
@Data
public class ClassCreateRequest {

    /**
     * 班级名称
     */
    @NotBlank(message = "班级名称不能为空")
    @Size(max = 100, message = "班级名称长度不能超过100个字符")
    private String className;

    /**
     * 年级
     */
    @NotNull(message = "年级不能为空")
    private String grade;

    /**
     * 班主任ID
     */
    private Long teacherId;

    /**
     * 班级描述
     */
    @Size(max = 500, message = "班级描述长度不能超过500个字符")
    private String description;

    /**
     * 班级状态
     */
    private ClassStatus status;
}