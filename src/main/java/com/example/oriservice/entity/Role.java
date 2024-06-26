package com.example.oriservice.entity;

import com.example.oriservice.enums.AuthorityRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "role")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private AuthorityRole name;

    @Column(name = "created_at")
    @CreationTimestamp
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void perPersist() {
        log.info("BaseEntity----perPersist-------");
        setCreatedBy("1");
    }

    @PreUpdate
    public void preUpdate() {
        log.info("BaseEntity----preUpdate-------");
        setUpdatedBy("1");
    }
}
