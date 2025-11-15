package com.school.cooperation.controller;

import com.school.cooperation.common.utils.Result;
import com.school.cooperation.dto.ClassCreateRequest;
import com.school.cooperation.dto.ClassUpdateRequest;
import com.school.cooperation.entity.EduClass;
import com.school.cooperation.entity.Student;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.ClassStatus;
import com.school.cooperation.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 班级管理控制器
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Validated
@Tag(name = "班级管理", description = "班级管理相关接口")
public class ClassController {

    private final ClassService classService;

    /**
     * 创建班级
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建班级", description = "创建新的班级")
    public Result<EduClass> createClass(@Valid @RequestBody ClassCreateRequest request) {
        EduClass eduClass = new EduClass();
        eduClass.setName(request.getClassName());
        eduClass.setGrade(request.getGrade());
        eduClass.setTeacherId(request.getTeacherId());
        eduClass.setDescription(request.getDescription());
        eduClass.setStatus(request.getStatus() != null ? request.getStatus() : ClassStatus.ACTIVE);

        EduClass createdClass = classService.createClass(eduClass);
        return Result.success(createdClass);
    }

    /**
     * 根据ID获取班级信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "根据ID获取班级信息", description = "根据班级ID获取班级详细信息")
    public Result<EduClass> getClassById(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

//        EduClass eduClass = classService.findById(id)
//                .orElseThrow(() -> new RuntimeException("班级不存在"));

        return Result.success(null);
    }

    /**
     * 更新班级信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "更新班级信息", description = "更新指定班级的信息")
    public Result<EduClass> updateClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ClassUpdateRequest request) {

        EduClass eduClass = new EduClass();
        eduClass.setId(id);
        eduClass.setName(request.getClassName());
        eduClass.setGrade(request.getGrade());
        eduClass.setTeacherId(request.getTeacherId());
        eduClass.setDescription(request.getDescription());
        eduClass.setStatus(request.getStatus());

        EduClass updatedClass = classService.updateClass(eduClass);
        return Result.success(updatedClass);
    }

    /**
     * 删除班级
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除班级", description = "删除指定班级")
    public Result<Void> deleteClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        classService.deleteClass(id);
        return Result.success();
    }

    /**
     * 获取所有班级
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "获取所有班级", description = "获取所有班级列表")
    public Result<List<EduClass>> getAllClasses() {
        List<EduClass> classes = classService.findAll();
        return Result.success(classes);
    }

    /**
     * 根据状态获取班级列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "根据状态获取班级列表", description = "根据班级状态获取班级列表")
    public Result<List<EduClass>> getClassesByStatus(
            @Parameter(description = "班级状态", required = true)
            @PathVariable ClassStatus status) {

        List<EduClass> classes = classService.findByStatus(status);
        return Result.success(classes);
    }

    /**
     * 根据年级获取班级列表
     */
    @GetMapping("/grade/{grade}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "根据年级获取班级列表", description = "根据年级获取班级列表")
    public Result<List<EduClass>> getClassesByGrade(
            @Parameter(description = "年级", required = true)
            @PathVariable Integer grade) {

        List<EduClass> classes = classService.findByGrade(grade);
        return Result.success(classes);
    }

    /**
     * 根据班主任获取班级列表
     */
    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "根据班主任获取班级列表", description = "根据班主任ID获取班级列表")
    public Result<List<EduClass>> getClassesByTeacher(
            @Parameter(description = "班主任ID", required = true)
            @PathVariable Long teacherId) {

        List<EduClass> classes = classService.findByTeacherId(teacherId);
        return Result.success(classes);
    }

    /**
     * 为班级分配班主任
     */
    @PutMapping("/{id}/assign-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "分配班主任", description = "为指定班级分配班主任")
    public Result<Void> assignTeacher(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @RequestParam Long teacherId) {

        classService.assignTeacher(id, teacherId);
        return Result.success();
    }

    /**
     * 移除班级班主任
     */
    @PutMapping("/{id}/remove-teacher")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "移除班主任", description = "移除指定班级的班主任")
    public Result<Void> removeTeacher(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        classService.removeTeacher(id);
        return Result.success();
    }

    /**
     * 获取班级的所有学生
     */
    @GetMapping("/{id}/students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "获取班级学生", description = "获取指定班级的所有学生")
    public Result<List<Student>> getStudentsByClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        List<Student> students = classService.getStudentsByClass(id);
        return Result.success(students);
    }

    /**
     * 获取班级的班主任信息
     */
    @GetMapping("/{id}/teacher")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "获取班级班主任", description = "获取指定班级的班主任信息")
    public Result<User> getTeacherByClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        Optional<User> teacher = classService.getTeacherByClass(id);
        return teacher.map(Result::success)
                .orElseThrow(() -> new RuntimeException("班级没有班主任"));
    }

    /**
     * 添加学生到班级
     */
    @PostMapping("/{id}/add-student")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "添加学生到班级", description = "将学生添加到指定班级")
    public Result<Void> addStudentToClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @RequestParam Long studentId) {

        classService.addStudentToClass(id, studentId);
        return Result.success();
    }

    /**
     * 从班级移除学生
     */
    @DeleteMapping("/{id}/remove-student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "从班级移除学生", description = "从指定班级移除学生")
    public Result<Void> removeStudentFromClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "学生ID", required = true)
            @PathVariable Long studentId) {

        classService.removeStudentFromClass(id, studentId);
        return Result.success();
    }

    /**
     * 批量添加学生到班级
     */
    @PostMapping("/{id}/add-students")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量添加学生到班级", description = "批量将学生添加到指定班级")
    public Result<Void> addStudentsToClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @RequestBody List<Long> studentIds) {

        classService.addStudentsToClass(id, studentIds);
        return Result.success();
    }

    /**
     * 批量从班级移除学生
     */
    @DeleteMapping("/{id}/remove-students")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量从班级移除学生", description = "批量从指定班级移除学生")
    public Result<Void> removeStudentsFromClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id,
            @RequestBody List<Long> studentIds) {

        classService.removeStudentsFromClass(id, studentIds);
        return Result.success();
    }

    /**
     * 激活班级
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "激活班级", description = "激活指定班级")
    public Result<Void> activateClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        classService.activateClass(id);
        return Result.success();
    }

    /**
     * 停用班级
     */
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "停用班级", description = "停用指定班级")
    public Result<Void> deactivateClass(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        classService.deactivateClass(id);
        return Result.success();
    }

    /**
     * 获取班级统计信息
     */
    @GetMapping("/{id}/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @Operation(summary = "获取班级统计信息", description = "获取指定班级的统计信息")
    public Result<Object> getClassStatistics(
            @Parameter(description = "班级ID", required = true)
            @PathVariable Long id) {

        Object statistics = classService.getClassStatistics(id);
        return Result.success(statistics);
    }
}