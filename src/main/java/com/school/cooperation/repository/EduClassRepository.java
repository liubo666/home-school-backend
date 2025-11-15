package com.school.cooperation.repository;

import com.school.cooperation.entity.EduClass;
import com.school.cooperation.entity.enums.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 班级Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface EduClassRepository extends JpaRepository<EduClass, Long>, JpaSpecificationExecutor<EduClass> {

    /**
     * 根据班级名称查询班级
     */
    Optional<EduClass> findByNameAndDeletedFalse(String name);

    /**
     * 根据班级名称查询（不检查删除状态）
     */
    Optional<EduClass> findByName(String name);

    /**
     * 检查班级名称是否存在
     */
    boolean existsByNameAndDeletedFalse(String name);

    /**
     * 根据年级查询班级列表
     */
    List<EduClass> findByGradeAndDeletedFalse(String grade);

    /**
     * 根据班主任查询班级列表
     */
    List<EduClass> findByTeacherIdAndDeletedFalse(Long teacherId);

    /**
     * 根据状态查询班级列表
     */
    List<EduClass> findByStatusAndDeletedFalse(ClassStatus status);

    /**
     * 根据年级和状态查询班级列表
     */
    List<EduClass> findByGradeAndStatusAndDeletedFalse(String grade, ClassStatus status);

    /**
     * 查询活跃班级列表
     */
    @Query("SELECT c FROM EduClass c WHERE c.status = 'ACTIVE' AND c.deleted = false ORDER BY c.grade, c.name")
    List<EduClass> findActiveClasses();

    /**
     * 根据班级名称模糊查询
     */
    @Query("SELECT c FROM EduClass c WHERE c.name LIKE %:className% AND c.deleted = false")
    List<EduClass> findByNameContaining(@Param("className") String className);

    /**
     * 根据多个条件查询班级
     */
    @Query("SELECT c FROM EduClass c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:grade IS NULL OR c.grade = :grade) AND " +
           "(:teacherId IS NULL OR c.teacherId = :teacherId) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "c.deleted = false")
    List<EduClass> findByConditions(@Param("name") String name,
                                   @Param("grade") String grade,
                                   @Param("teacherId") Long teacherId,
                                   @Param("status") ClassStatus status);

    /**
     * 统计各年级班级数量
     */
    @Query("SELECT c.grade, COUNT(c) FROM EduClass c WHERE c.status = 'ACTIVE' AND c.deleted = false GROUP BY c.grade")
    List<Object[]> countClassesByGrade();

    /**
     * 统计班主任管理的班级数量
     */
    @Query("SELECT c.teacherId, COUNT(c) FROM EduClass c WHERE c.status = 'ACTIVE' AND c.deleted = false GROUP BY c.teacherId")
    List<Object[]> countClassesByTeacher();

    /**
     * 查询指定班主任的所有班级
     */
    @Query("SELECT c FROM EduClass c WHERE c.teacherId = :teacherId AND c.status = 'ACTIVE' AND c.deleted = false")
    List<EduClass> findTeacherActiveClasses(@Param("teacherId") Long teacherId);

    /**
     * 更新班级学生数量
     */
    @Query("UPDATE EduClass c SET c.studentCount = :count WHERE c.id = :classId")
    void updateStudentCount(@Param("classId") Long classId, @Param("count") Integer count);

    /**
     * 增加班级学生数量
     */
    @Query("UPDATE EduClass c SET c.studentCount = c.studentCount + :increment WHERE c.id = :classId")
    void incrementStudentCount(@Param("classId") Long classId, @Param("increment") Integer increment);

    /**
     * 减少班级学生数量
     */
    @Query("UPDATE EduClass c SET c.studentCount = c.studentCount - :decrement WHERE c.id = :classId AND c.studentCount >= :decrement")
    void decrementStudentCount(@Param("classId") Long classId, @Param("decrement") Integer decrement);

    /**
     * 查询学生人数为0的班级
     */
    @Query("SELECT c FROM EduClass c WHERE c.studentCount = 0 AND c.status = 'ACTIVE' AND c.deleted = false")
    List<EduClass> findEmptyClasses();

    /**
     * 查询学生人数超过指定数量的班级
     */
    @Query("SELECT c FROM EduClass c WHERE c.studentCount > :maxCount AND c.status = 'ACTIVE' AND c.deleted = false")
    List<EduClass> findClassesWithMoreStudents(@Param("maxCount") Integer maxCount);

    /**
     * 查询所有不重复的年级列表
     */
    @Query("SELECT DISTINCT c.grade FROM EduClass c WHERE c.status = 'ACTIVE' AND c.deleted = false ORDER BY c.grade")
    List<String> findAllGrades();

    /**
     * 查询没有班主任的班级
     */
    @Query("SELECT c FROM EduClass c WHERE c.teacherId IS NULL AND c.status = 'ACTIVE' AND c.deleted = false")
    List<EduClass> findClassesWithoutTeacher();
}