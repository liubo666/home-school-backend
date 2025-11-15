package com.school.cooperation.repository;

import com.school.cooperation.entity.StudentRecord;
import com.school.cooperation.entity.enums.Importance;
import com.school.cooperation.entity.enums.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学生档案记录Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface StudentRecordRepository extends JpaRepository<StudentRecord, Long>, JpaSpecificationExecutor<StudentRecord> {

    /**
     * 根据学生ID查询记录列表
     */
    List<StudentRecord> findByStudentIdAndDeletedFalse(Long studentId);

    /**
     * 根据教师ID查询记录列表
     */
    List<StudentRecord> findByTeacherIdAndDeletedFalse(Long teacherId);

    /**
     * 根据记录类型查询记录列表
     */
    List<StudentRecord> findByTypeAndDeletedFalse(RecordType type);

    /**
     * 根据学生ID和记录类型查询
     */
    List<StudentRecord> findByStudentIdAndTypeAndDeletedFalse(Long studentId, RecordType type);

    /**
     * 根据教师ID和记录类型查询
     */
    List<StudentRecord> findByTeacherIdAndTypeAndDeletedFalse(Long teacherId, RecordType type);

    /**
     * 根据记录分类查询
     */
    List<StudentRecord> findByCategoryAndDeletedFalse(String category);

    /**
     * 根据重要程度查询
     */
    List<StudentRecord> findByImportanceAndDeletedFalse(Importance importance);

    /**
     * 查询对家长公开的记录
     */
    List<StudentRecord> findByIsPublicTrueAndDeletedFalse();

    /**
     * 查询未通知家长的记录
     */
    List<StudentRecord> findByIsPublicTrueAndParentNotifiedFalseAndDeletedFalse();

    /**
     * 根据多个条件查询记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE " +
           "(:studentId IS NULL OR sr.studentId = :studentId) AND " +
           "(:teacherId IS NULL OR sr.teacherId = :teacherId) AND " +
           "(:type IS NULL OR sr.type = :type) AND " +
           "(:category IS NULL OR sr.category LIKE %:category%) AND " +
           "(:title IS NULL OR sr.title LIKE %:title%) AND " +
           "(:importance IS NULL OR sr.importance = :importance) AND " +
           "(:isPublic IS NULL OR sr.isPublic = :isPublic) AND " +
           "(:startDate IS NULL OR sr.createdTime >= :startDate) AND " +
           "(:endDate IS NULL OR sr.createdTime <= :endDate) AND " +
           "sr.deleted = false")
    List<StudentRecord> findByConditions(@Param("studentId") Long studentId,
                                        @Param("teacherId") Long teacherId,
                                        @Param("type") RecordType type,
                                        @Param("category") String category,
                                        @Param("title") String title,
                                        @Param("importance") Importance importance,
                                        @Param("isPublic") Boolean isPublic,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);

    /**
     * 统计各类型记录数量
     */
    @Query("SELECT sr.type, COUNT(sr) FROM StudentRecord sr WHERE sr.deleted = false GROUP BY sr.type")
    List<Object[]> countRecordsByType();

    /**
     * 统计各重要程度记录数量
     */
    @Query("SELECT sr.importance, COUNT(sr) FROM StudentRecord sr WHERE sr.deleted = false GROUP BY sr.importance")
    List<Object[]> countRecordsByImportance();

    /**
     * 统计指定学生的各类型记录数量
     */
    @Query("SELECT sr.type, COUNT(sr) FROM StudentRecord sr WHERE sr.studentId = :studentId AND sr.deleted = false GROUP BY sr.type")
    List<Object[]> countRecordsByTypeForStudent(@Param("studentId") Long studentId);

    /**
     * 查询指定时间范围内的记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.createdTime BETWEEN :startTime AND :endTime AND sr.deleted = false")
    List<StudentRecord> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 查询高重要程度的记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.importance = 'HIGH' AND sr.isPublic = true AND sr.parentNotified = false AND sr.deleted = false")
    List<StudentRecord> findHighImportanceUnnotifiedRecords();

    /**
     * 根据班级查询学生记录（需要关联学生表）
     */
    @Query("SELECT sr FROM StudentRecord sr JOIN Student s ON sr.studentId = s.id WHERE s.classId = :classId AND sr.deleted = false AND s.deleted = false")
    List<StudentRecord> findByClassId(@Param("classId") Long classId);

    /**
     * 查询指定教师的记录统计
     */
    @Query("SELECT sr.teacherId, COUNT(sr) FROM StudentRecord sr WHERE sr.teacherId = :teacherId AND sr.deleted = false GROUP BY sr.teacherId")
    Long countRecordsByTeacher(@Param("teacherId") Long teacherId);

    /**
     * 查询最近的记录（指定数量）
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.deleted = false ORDER BY sr.createdTime DESC")
    List<StudentRecord> findRecentRecords();

    /**
     * 查询指定学生最近的记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.studentId = :studentId AND sr.deleted = false ORDER BY sr.createdTime DESC")
    List<StudentRecord> findRecentRecordsByStudent(@Param("studentId") Long studentId);

    /**
     * 查询表扬记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.type = 'PRAISE' AND sr.deleted = false ORDER BY sr.createdTime DESC")
    List<StudentRecord> findPraiseRecords();

    /**
     * 查询批评记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.type = 'CRITICISM' AND sr.deleted = false ORDER BY sr.createdTime DESC")
    List<StudentRecord> findCriticismRecords();

    /**
     * 批量查询学生的记录数量
     */
    @Query("SELECT sr.studentId, COUNT(sr) FROM StudentRecord sr WHERE sr.studentId IN :studentIds AND sr.deleted = false GROUP BY sr.studentId")
    List<Object[]> countRecordsByStudentIds(@Param("studentIds") List<Long> studentIds);

    /**
     * 查询需要通知家长的记录
     */
    @Query("SELECT sr FROM StudentRecord sr WHERE sr.isPublic = true AND sr.parentNotified = false AND sr.deleted = false")
    List<StudentRecord> findRecordsRequiringNotification();

    /**
     * 更新记录通知状态
     */
    @Query("UPDATE StudentRecord sr SET sr.parentNotified = true WHERE sr.id IN :recordIds")
    void updateNotificationStatus(@Param("recordIds") List<Long> recordIds);

    /**
     * 查询所有不重复的记录分类
     */
    @Query("SELECT DISTINCT sr.category FROM StudentRecord sr WHERE sr.deleted = false ORDER BY sr.category")
    List<String> findAllCategories();
}