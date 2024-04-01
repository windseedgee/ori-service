package com.example.oriservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

@Entity
@Table(name="hero")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
//@EntityListeners(AuditingEntityListener.class)
public class Hero extends BaseEntity{

    @Column(name = "name")
    private String name;

//    @Column(name = "created_at")
//    @CreationTimestamp
//    @CreatedDate
//    private Instant createdAt;
//
//    @NotNull
//    @Column(name = "created_by")
//    @CreatedBy
//    private String createdBy;
//
//    @Column(name = "updated_at")
////    @UpdateTimestamp
//    @LastModifiedDate
//    private Instant updatedAt;
//
//    @Column(name = "updated_by")
//    @LastModifiedBy
//    private String updatedBy;
//
//    @Column(name = "deleted_at")
//    private Instant deletedAt;

    @Column(name = "auto_renewal")
    private Boolean autoRenewal;

    @Override
    public void perPersist() {
        super.perPersist();
        log.info("Hero-----perPersist-------");
//        if (StringUtils.isBlank(createdBy)) {
//            setCreatedBy("111");
//        }
//        if (StringUtils.isBlank(updatedBy)) {
//            setUpdatedBy("222");
//        }
        if(autoRenewal == null){
            autoRenewal = true;
        }
    }

/*    @PreUpdate
    public void preUpdate() {
        log.info("preUpdate-------");
            setUpdatedAt(Instant.now());
            setUpdatedBy("2");

    }

    @PreRemove
    public void preRemove() {
        log.info("preRemove-------");

            setDeletedAt(Instant.now());

    }

    @PostLoad
    public void postLoad() {
        log.info("postLoad-------");
    }*/

}

