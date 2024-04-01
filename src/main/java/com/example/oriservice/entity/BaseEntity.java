package com.example.oriservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;




@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
//@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

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
//    @PreUpdate
    public void perPersist() {
        log.info("BaseEntity----perPersist-------");
        setCreatedAt(Instant.now());
        setCreatedBy("1");
        setUpdatedAt(Instant.now());
        setUpdatedBy("1");
    }

    @PreUpdate
    public void preUpdate() {
        log.info("BaseEntity----preUpdate-------");
        setUpdatedAt(Instant.now());
        setUpdatedBy("1");
    }

    @PreRemove
    public void preRemove() {
        log.info("BaseEntity----preRemove-------");

    }

    @PostLoad
    public void postLoad() {
        log.info("BaseEntity----postLoad-------");
    }

}

