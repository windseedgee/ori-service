package com.example.oriservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;

@Entity
@Table(name="student")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
//@EntityListeners(AuditingEntityListener.class)
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", columnDefinition = "VARCHAR DEFAULT 'ori'")
    private String name;

//    @PrePersist
//    @PreUpdate
//    public void perPersist() {
//        log.info("perPersist-------");
//        if (StringUtils.isBlank(createdBy)) {
//            setCreatedBy("111");
//        }
//        if (StringUtils.isBlank(updatedBy)) {
//            setUpdatedBy("222");
//        }
//        if(StringUtils.isBlank(name)){
//            setName("ori");
//        }
//    }

//    @PreUpdate
//    public void preUpdate() {
//        log.info("preUpdate-------");
//            setUpdatedAt(Instant.now());
//            setUpdatedBy("2");
//
//    }

//    @PreRemove
//    public void preRemove() {
//        log.info("preRemove-------");
//
//            setDeletedAt(Instant.now());
//
//    }
//
//    @PostLoad
//    public void postLoad() {
//        log.info("postLoad-------");
//    }

}

