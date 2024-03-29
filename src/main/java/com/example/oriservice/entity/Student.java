package com.example.oriservice.entity;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="student")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
@EntityListeners(AuditingEntityListener.class)
public class Student implements Serializable{

    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", columnDefinition = "VARCHAR DEFAULT 'ori'")
    private String name;

    @Column(name = "created_at")
    @CreationTimestamp
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_at")
//    @UpdateTimestamp
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    @PreUpdate
    public void perPersist() {
        log.info("perPersist-------");
        if (StringUtils.isBlank(createdBy)) {
            setCreatedBy("111");
        }
        if (StringUtils.isBlank(updatedBy)) {
            setUpdatedBy("222");
        }
//        if(StringUtils.isBlank(name)){
//            setName("ori");
//        }
    }

//    @PreUpdate
//    public void preUpdate() {
//        log.info("preUpdate-------");
//            setUpdatedAt(Instant.now());
//            setUpdatedBy("2");
//
//    }

    @PreRemove
    public void preRemove() {
        log.info("preRemove-------");

            setDeletedAt(Instant.now());

    }

    @PostLoad
    public void postLoad() {
        log.info("postLoad-------");
    }

}

