package com.example.oriservice.service;

import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Student;
import java.util.UUID;

public interface EventService {
    void handle(String message);
}
