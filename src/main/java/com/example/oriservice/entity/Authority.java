package com.example.oriservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "authority")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
public class Authority extends BaseEntity {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "role_id")
    private UUID roleId;
}
