package com.school.cooperation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含公共字段：创建时间、更新时间、创建人、更新人、逻辑删除标识
 *
 * @author system
 * @since 2025-11-15
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 逻辑删除标识 (0:未删除, 1:已删除)
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    /**
     * JPA生命周期回调 - 创建前设置时间
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdTime = now;
        updatedTime = now;
    }

    /**
     * JPA生命周期回调 - 更新前设置时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }

    /**
     * 是否已删除
     *
     * @return true if deleted, false otherwise
     */
    public Boolean isDeleted() {
        return this.deleted != null && this.deleted;
    }
}