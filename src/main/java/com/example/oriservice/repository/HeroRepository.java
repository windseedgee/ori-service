package com.example.oriservice.repository;

import com.example.oriservice.entity.Hero;
import com.example.oriservice.entity.Student;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, UUID>, JpaSpecificationExecutor<Hero> {

}
