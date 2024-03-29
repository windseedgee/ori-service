package com.example.oriservice.service;

import com.example.oriservice.dto.HeroDto;
import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Hero;
import com.example.oriservice.entity.Student;
import java.util.UUID;

public interface HeroService {
    void create(HeroDto user);

    void updateUser(HeroDto user);

    Hero getUserById(UUID id);

    void deleteUser(UUID uuid);
}
