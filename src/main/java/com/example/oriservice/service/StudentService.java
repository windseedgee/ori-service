package com.example.oriservice.service;

import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Student;
import java.util.UUID;

public interface StudentService {
    void createUser(StudentDto user);

    void updateUser(StudentDto user);

    Student getUserById(UUID id);

    void deleteUser(UUID uuid);
}
