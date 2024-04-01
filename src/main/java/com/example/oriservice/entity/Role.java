package com.example.oriservice.entity;

import com.example.oriservice.enums.AuthorityRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "role")
@Where(clause = "deleted_at is null")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
public class Role extends BaseEntity {

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private AuthorityRole name;


}
