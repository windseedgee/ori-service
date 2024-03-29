package com.example.oriservice.service;

import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Student;
import com.example.oriservice.repository.StudentRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final EventDispatcher eventDispatcher;

    @Override
    public void createUser(StudentDto user) {
        Student newStudent = new Student();
        newStudent.setName(user.getName());
        studentRepository.save(newStudent);
    }

    @Override
    public void updateUser(StudentDto user) {
        Student updateStudent = studentRepository.findById(user.getId()).get();
        updateStudent.setName(user.getName());
        studentRepository.save(updateStudent);
    }

    @Override
    public Student getUserById(UUID id) {
        log.info("first");
        studentRepository.findById(id);
        eventDispatcher.dispatchEvent("test");
        return studentRepository.findById(id).get();
    }

    @Override
    public void deleteUser(UUID uuid) {
        studentRepository.deleteById(uuid);
    }
}
