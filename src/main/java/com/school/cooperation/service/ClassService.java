package com.school.cooperation.service;

import com.school.cooperation.entity.EduClass;
import com.school.cooperation.entity.Student;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.ClassStatus;

import java.util.List;
import java.util.Optional;

/**
 * 班级管理服务接口
 *
 * @author homeschool
 * @since 1.0.0
 */
public interface ClassService {





    /**
     * 创建班级
     *
     * @param eduClass 班级信息
     * @return 创建后的班级信息
     */
    EduClass createClass(EduClass eduClass);

    /**
     * 更新班级信息
     *
     * @param eduClass 班级信息
     * @return 更新后的班级信息
     */
    EduClass updateClass(EduClass eduClass);

    /**
     * 修改班级状态
     *
     * @param classId 班级ID
     * @param status 新状态
     */
    void updateClassStatus(Long classId, ClassStatus status);

    /**
     * 根据状态获取班级列表
     *
     * @param status 状态
     * @return 班级列表
     */
    List<EduClass> findByStatus(ClassStatus status);

    /**
     * 根据班主任获取班级列表
     *
     * @param teacherId 班主任ID
     * @return 班级列表
     */
    List<EduClass> findByTeacherId(Long teacherId);

    /**
     * 根据年级获取班级列表
     *
     * @param grade 年级
     * @return 班级列表
     */
    List<EduClass> findByGrade(Integer grade);

    /**
     * 获取所有班级
     *
     * @return 班级列表
     */
    List<EduClass> findAll();

    /**
     * 检查班级名称是否存在
     *
     * @param className 班级名称
     * @return 是否存在
     */
    boolean existsByClassName(String className);

    /**
     * 为班级分配班主任
     *
     * @param classId 班级ID
     * @param teacherId 教师ID
     */
    void assignTeacher(Long classId, Long teacherId);

    /**
     * 移除班级班主任
     *
     * @param classId 班级ID
     */
    void removeTeacher(Long classId);

    /**
     * 添加学生到班级
     *
     * @param classId 班级ID
     * @param studentId 学生ID
     */
    void addStudentToClass(Long classId, Long studentId);

    /**
     * 从班级移除学生
     *
     * @param classId 班级ID
     * @param studentId 学生ID
     */
    void removeStudentFromClass(Long classId, Long studentId);

    /**
     * 获取班级的所有学生
     *
     * @param classId 班级ID
     * @return 学生列表
     */
    List<Student> getStudentsByClass(Long classId);

    /**
     * 获取班级的班主任信息
     *
     * @param classId 班级ID
     * @return 班主任信息
     */
    Optional<User> getTeacherByClass(Long classId);

    /**
     * 批量添加学生到班级
     *
     * @param classId 班级ID
     * @param studentIds 学生ID列表
     */
    void addStudentsToClass(Long classId, List<Long> studentIds);

    /**
     * 批量从班级移除学生
     *
     * @param classId 班级ID
     * @param studentIds 学生ID列表
     */
    void removeStudentsFromClass(Long classId, List<Long> studentIds);

    /**
     * 激活班级
     *
     * @param classId 班级ID
     */
    void activateClass(Long classId);

    /**
     * 停用班级
     *
     * @param classId 班级ID
     */
    void deactivateClass(Long classId);

    /**
     * 删除班级
     *
     * @param classId 班级ID
     */
    void deleteClass(Long classId);

    /**
     * 获取班级统计信息
     *
     * @param classId 班级ID
     * @return 统计信息
     */
    Object getClassStatistics(Long classId);


    public boolean existsByName(String className);
}