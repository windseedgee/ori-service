package com.example.oriservice.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
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

    @PrePersist
    @PreUpdate
    public void perPersist() {
        log.info("BaseEntity----perPersist-------");
    }

    @PreUpdate
    public void preUpdate() {
        log.info("BaseEntity----preUpdate-------");
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

