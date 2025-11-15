package com.school.cooperation.repository;

import com.school.cooperation.entity.Notification;
import com.school.cooperation.entity.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 根据用户ID查询通知列表
     */
    List<Notification> findByUserIdOrderByCreatedTimeDesc(Long userId);

    /**
     * 根据用户ID查询未读通知
     */
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedTimeDesc(Long userId);

    /**
     * 根据用户ID和通知类型查询
     */
    List<Notification> findByUserIdAndTypeOrderByCreatedTimeDesc(Long userId, NotificationType type);

    /**
     * 根据通知类型查询所有通知
     */
    List<Notification> findByTypeOrderByCreatedTimeDesc(NotificationType type);

    /**
     * 统计用户的未读通知数量
     */
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.isRead = false")
    Long countUnreadNotifications(@Param("userId") Long userId);

    /**
     * 统计各类型通知数量
     */
    @Query("SELECT n.type, COUNT(n) FROM Notification n GROUP BY n.type")
    List<Object[]> countNotificationsByType();

    /**
     * 统计指定用户的各类型通知数量
     */
    @Query("SELECT n.type, COUNT(n) FROM Notification n WHERE n.userId = :userId GROUP BY n.type")
    List<Object[]> countNotificationsByTypeForUser(@Param("userId") Long userId);

    /**
     * 查询指定时间范围内的通知
     */
    @Query("SELECT n FROM Notification n WHERE n.createdTime BETWEEN :startTime AND :endTime ORDER BY n.createdTime DESC")
    List<Notification> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    /**
     * 查询指定时间后的通知
     */
    @Query("SELECT n FROM Notification n WHERE n.createdTime >= :startTime ORDER BY n.createdTime DESC")
    List<Notification> findByCreatedTimeAfter(@Param("startTime") LocalDateTime startTime);

    /**
     * 根据关联ID查询通知
     */
    List<Notification> findByRelatedIdAndTypeOrderByCreatedTimeDesc(Long relatedId, NotificationType type);

    /**
     * 批量标记通知为已读
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readTime = :readTime WHERE n.id IN :notificationIds")
    void markAsRead(@Param("notificationIds") List<Long> notificationIds, @Param("readTime") LocalDateTime readTime);

    /**
     * 标记用户所有通知为已读
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readTime = :readTime WHERE n.userId = :userId AND n.isRead = false")
    void markAllAsReadForUser(@Param("userId") Long userId, @Param("readTime") LocalDateTime readTime);

    /**
     * 根据多个条件查询通知
     */
    @Query("SELECT n FROM Notification n WHERE " +
           "(:userId IS NULL OR n.userId = :userId) AND " +
           "(:type IS NULL OR n.type = :type) AND " +
           "(:isRead IS NULL OR n.isRead = :isRead) AND " +
           "(:relatedId IS NULL OR n.relatedId = :relatedId) AND " +
           "(:startDate IS NULL OR n.createdTime >= :startDate) AND " +
           "(:endDate IS NULL OR n.createdTime <= :endDate) AND " +
           "(:title IS NULL OR n.title LIKE %:title%)")
    List<Notification> findByConditions(@Param("userId") Long userId,
                                       @Param("type") NotificationType type,
                                       @Param("isRead") Boolean isRead,
                                       @Param("relatedId") Long relatedId,
                                       @Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate,
                                       @Param("title") String title);

    /**
     * 删除指定时间之前的通知
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdTime < :beforeTime")
    void deleteNotificationsBefore(@Param("beforeTime") LocalDateTime beforeTime);

    /**
     * 查询系统通知
     */
    @Query("SELECT n FROM Notification n WHERE n.type = 'SYSTEM' ORDER BY n.createdTime DESC")
    List<Notification> findSystemNotifications();

    /**
     * 查询指定用户的系统通知
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.type = 'SYSTEM' ORDER BY n.createdTime DESC")
    List<Notification> findSystemNotificationsForUser(@Param("userId") Long userId);

    /**
     * 查询记录相关通知
     */
    @Query("SELECT n FROM Notification n WHERE n.type = 'RECORD' AND n.relatedId = :recordId ORDER BY n.createdTime DESC")
    List<Notification> findRecordNotifications(@Param("recordId") Long recordId);

    /**
     * 查询班级相关通知
     */
    @Query("SELECT n FROM Notification n WHERE n.type = 'CLASS' AND n.relatedId = :classId ORDER BY n.createdTime DESC")
    List<Notification> findClassNotifications(@Param("classId") Long classId);

    /**
     * 查询学生相关通知
     */
    @Query("SELECT n FROM Notification n WHERE n.type = 'STUDENT' AND n.relatedId = :studentId ORDER BY n.createdTime DESC")
    List<Notification> findStudentNotifications(@Param("studentId") Long studentId);

    /**
     * 查询最近的通知
     */
    @Query("SELECT n FROM Notification n ORDER BY n.createdTime DESC")
    List<Notification> findRecentNotifications();

    /**
     * 查询指定用户的最近通知
     */
    @Query("SELECT n FROM Notification n WHERE n.userId = :userId ORDER BY n.createdTime DESC")
    List<Notification> findRecentNotificationsForUser(@Param("userId") Long userId);

    /**
     * 统计每日通知数量
     */
    @Query("SELECT DATE(n.createdTime), COUNT(n) FROM Notification n WHERE n.createdTime >= :startDate GROUP BY DATE(n.createdTime)")
    List<Object[]> countNotificationsByDay(@Param("startDate") LocalDateTime startDate);

    /**
     * 查询未通知的高重要记录（用于生成通知）
     */
    @Query("SELECT n FROM Notification n WHERE n.isRead = false AND n.type = 'RECORD' AND n.createdTime >= :since ORDER BY n.createdTime DESC")
    List<Notification> findUnreadRecordNotifications(@Param("since") LocalDateTime since);
}