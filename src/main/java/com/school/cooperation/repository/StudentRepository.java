package com.school.cooperation.repository;

import com.school.cooperation.entity.Student;
import com.school.cooperation.entity.enums.Gender;
import com.school.cooperation.entity.enums.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 学生Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    /**
     * 根据学号查询学生
     */
    Optional<Student> findByStudentIdAndDeletedFalse(String studentId);

    /**
     * 根据学号查询（不检查删除状态）
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * 根据身份证号查询学生
     */
    Optional<Student> findByIdCardAndDeletedFalse(String idCard);

    /**
     * 检查学号是否存在
     */
    boolean existsByStudentIdAndDeletedFalse(String studentId);

    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCardAndDeletedFalse(String idCard);

    /**
     * 根据班级ID查询学生列表
     */
    List<Student> findByClassIdAndDeletedFalse(Long classId);

    /**
     * 根据班级ID查询学生列表（不检查删除状态）
     */
    List<Student> findByClassId(Long classId);

    /**
     * 根据状态查询学生列表
     */
    List<Student> findByStatusAndDeletedFalse(StudentStatus status);

    /**
     * 根据性别查询学生列表
     */
    List<Student> findByGenderAndDeletedFalse(Gender gender);

    /**
     * 根据班级和状态查询学生列表
     */
    List<Student> findByClassIdAndStatusAndDeletedFalse(Long classId, StudentStatus status);

    /**
     * 根据姓名模糊查询学生
     */
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:name% AND s.deleted = false")
    List<Student> findByNameContaining(@Param("name") String name);

    /**
     * 查询在读学生列表
     */
    @Query("SELECT s FROM Student s WHERE s.status = 'ACTIVE' AND s.deleted = false ORDER BY s.classId, s.name")
    List<Student> findActiveStudents();

    /**
     * 根据多个条件查询学生
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(:studentId IS NULL OR s.studentId LIKE %:studentId%) AND " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:idCard IS NULL OR s.idCard LIKE %:idCard%) AND " +
           "(:classId IS NULL OR s.classId = :classId) AND " +
           "(:gender IS NULL OR s.gender = :gender) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "s.deleted = false")
    List<Student> findByConditions(@Param("studentId") String studentId,
                                 @Param("name") String name,
                                 @Param("idCard") String idCard,
                                 @Param("classId") Long classId,
                                 @Param("gender") Gender gender,
                                 @Param("status") StudentStatus status);

    /**
     * 统计各班级学生数量
     */
    @Query("SELECT s.classId, COUNT(s) FROM Student s WHERE s.status = 'ACTIVE' AND s.deleted = false GROUP BY s.classId")
    List<Object[]> countStudentsByClass();

    /**
     * 统计各状态学生数量
     */
    @Query("SELECT s.status, COUNT(s) FROM Student s WHERE s.deleted = false GROUP BY s.status")
    List<Object[]> countStudentsByStatus();

    /**
     * 统计各性别学生数量
     */
    @Query("SELECT s.gender, COUNT(s) FROM Student s WHERE s.status = 'ACTIVE' AND s.deleted = false GROUP BY s.gender")
    List<Object[]> countActiveStudentsByGender();

    /**
     * 查询指定日期之后入学的学生
     */
    @Query("SELECT s FROM Student s WHERE s.enrollmentDate >= :startDate AND s.deleted = false")
    List<Student> findStudentsEnrolledAfter(@Param("startDate") LocalDate startDate);

    /**
     * 查询指定日期范围内出生的学生
     */
    @Query("SELECT s FROM Student s WHERE s.birthDate BETWEEN :startDate AND :endDate AND s.deleted = false")
    List<Student> findStudentsByBirthDateRange(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * 查询即将过生日的学生（指定天数内）
     */
    @Query("SELECT s FROM Student s WHERE " +
           "MONTH(s.birthDate) = MONTH(CURRENT_DATE) AND " +
           "DAY(s.birthDate) BETWEEN DAY(CURRENT_DATE) AND DAY(CURRENT_DATE) + :days AND " +
           "s.status = 'ACTIVE' AND s.deleted = false")
    List<Student> findStudentsWithUpcomingBirthday(@Param("days") Integer days);

    /**
     * 查询指定年龄范围的学生
     */
    @Query("SELECT s FROM Student s WHERE " +
           "YEAR(CURRENT_DATE) - YEAR(s.birthDate) BETWEEN :minAge AND :maxAge AND " +
           "s.status = 'ACTIVE' AND s.deleted = false")
    List<Student> findStudentsByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    /**
     * 查询没有紧急联系信息的学生
     */
    @Query("SELECT s FROM Student s WHERE " +
           "(s.emergencyContactName IS NULL OR s.emergencyContactPhone IS NULL) AND " +
           "s.status = 'ACTIVE' AND s.deleted = false")
    List<Student> findStudentsWithoutEmergencyContact();

    /**
     * 查询指定班级的在读学生数量
     */
    @Query("SELECT COUNT(s) FROM Student s WHERE s.classId = :classId AND s.status = 'ACTIVE' AND s.deleted = false")
    Long countActiveStudentsByClass(@Param("classId") Long classId);

    /**
     * 根据紧急联系人手机号查询学生
     */
    @Query("SELECT s FROM Student s WHERE s.emergencyContactPhone = :phone AND s.deleted = false")
    List<Student> findByEmergencyContactPhone(@Param("phone") String phone);

    /**
     * 批量查询学号是否存在
     */
    @Query("SELECT s.studentId FROM Student s WHERE s.studentId IN :studentIds AND s.deleted = false")
    List<String> findExistingStudentIds(@Param("studentIds") List<String> studentIds);

    /**
     * 查询毕业的学生
     */
    @Query("SELECT s FROM Student s WHERE s.status = 'GRADUATED' AND s.deleted = false ORDER BY s.updatedTime DESC")
    List<Student> findGraduatedStudents();

    /**
     * 查询转出的学生
     */
    @Query("SELECT s FROM Student s WHERE s.status = 'TRANSFERRED' AND s.deleted = false ORDER BY s.updatedTime DESC")
    List<Student> findTransferredStudents();
}