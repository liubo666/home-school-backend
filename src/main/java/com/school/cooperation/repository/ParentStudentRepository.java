package com.school.cooperation.repository;

import com.school.cooperation.entity.ParentStudent;
import com.school.cooperation.entity.enums.ParentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 家长学生关联Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudent, Long>, JpaSpecificationExecutor<ParentStudent> {

    /**
     * 根据家长ID和学生ID查询关联关系
     */
    Optional<ParentStudent> findByParentIdAndStudentId(Long parentId, Long studentId);

    /**
     * 根据家长ID查询关联的学生列表
     */
    List<ParentStudent> findByParentId(Long parentId);

    /**
     * 根据学生ID查询关联的家长列表
     */
    List<ParentStudent> findByStudentId(Long studentId);

    /**
     * 根据家长ID查询主要联系人关系
     */
    List<ParentStudent> findByParentIdAndIsPrimaryTrue(Long parentId);

    /**
     * 根据学生ID查询主要联系人关系
     */
    List<ParentStudent> findByStudentIdAndIsPrimaryTrue(Long studentId);

    /**
     * 根据关系类型查询
     */
    List<ParentStudent> findByRelation(ParentRelation relation);

    /**
     * 根据家长ID和关系类型查询
     */
    List<ParentStudent> findByParentIdAndRelation(Long parentId, ParentRelation relation);

    /**
     * 根据学生ID和关系类型查询
     */
    List<ParentStudent> findByStudentIdAndRelation(Long studentId, ParentRelation relation);

    /**
     * 检查家长学生关联是否存在
     */
    boolean existsByParentIdAndStudentId(Long parentId, Long studentId);

    /**
     * 统计家长关联的学生数量
     */
    @Query("SELECT COUNT(ps) FROM ParentStudent ps WHERE ps.parentId = :parentId")
    Long countStudentsByParent(@Param("parentId") Long parentId);

    /**
     * 统计学生关联的家长数量
     */
    @Query("SELECT COUNT(ps) FROM ParentStudent ps WHERE ps.studentId = :studentId")
    Long countParentsByStudent(@Param("studentId") Long studentId);

    /**
     * 统计各关系类型的数量
     */
    @Query("SELECT ps.relation, COUNT(ps) FROM ParentStudent ps GROUP BY ps.relation")
    List<Object[]> countByRelation();

    /**
     * 查询没有主要联系人的学生
     */
    @Query("SELECT ps.studentId FROM ParentStudent ps WHERE ps.isPrimary = true GROUP BY ps.studentId")
    List<Long> findStudentsWithPrimaryContact();

    /**
     * 查询没有主要联系人的学生ID列表
     */
    @Query("SELECT s.id FROM Student s WHERE s.status = 'ACTIVE' AND s.deleted = false AND " +
           "s.id NOT IN (SELECT ps.studentId FROM ParentStudent ps WHERE ps.isPrimary = true)")
    List<Long> findStudentsWithoutPrimaryContact();

    /**
     * 根据家长ID查询所有学生ID
     */
    @Query("SELECT ps.studentId FROM ParentStudent ps WHERE ps.parentId = :parentId")
    List<Long> findStudentIdsByParent(@Param("parentId") Long parentId);

    /**
     * 根据学生ID查询所有家长ID
     */
    @Query("SELECT ps.parentId FROM ParentStudent ps WHERE ps.studentId = :studentId")
    List<Long> findParentIdsByStudent(@Param("studentId") Long studentId);

    /**
     * 设置指定学生的所有家长为非主要联系人
     */
    @Query("UPDATE ParentStudent ps SET ps.isPrimary = false WHERE ps.studentId = :studentId")
    void clearPrimaryContactForStudent(@Param("studentId") Long studentId);

    /**
     * 设置指定家长的所有关联为非主要联系人
     */
    @Query("UPDATE ParentStudent ps SET ps.isPrimary = false WHERE ps.parentId = :parentId")
    void clearPrimaryContactForParent(@Param("parentId") Long parentId);

    /**
     * 批量查询家长学生关联关系
     */
    List<ParentStudent> findByParentIdIn(List<Long> parentIds);

    /**
     * 批量查询学生家长关联关系
     */
    List<ParentStudent> findByStudentIdIn(List<Long> studentIds);

    /**
     * 查询指定关系类型的家长学生对
     */
    @Query("SELECT ps FROM ParentStudent ps WHERE ps.relation = :relation")
    List<ParentStudent> findByRelationType(@Param("relation") ParentRelation relation);

    /**
     * 删除学生与所有家长的关联
     */
    @Modifying
    @Query("DELETE FROM ParentStudent ps WHERE ps.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") Long studentId);

    /**
     * 删除家长与所有学生的关联
     */
    @Modifying
    @Query("DELETE FROM ParentStudent ps WHERE ps.parentId = :parentId")
    void deleteByParentId(@Param("parentId") Long parentId);
}