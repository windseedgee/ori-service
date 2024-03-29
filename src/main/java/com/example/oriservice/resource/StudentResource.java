package com.example.oriservice.resource;


import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Student;
import com.example.oriservice.service.StudentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class StudentResource {

    private final StudentService studentService;

    @GetMapping("/{id}")
    public Student getUserById(@PathVariable(value = "id")UUID uuid){
        return studentService.getUserById(uuid);
    }

    @PostMapping
    public void createUser(@RequestBody StudentDto studentDto){
        studentService.createUser(studentDto);
    }

    @PutMapping
    public void updateUser(@RequestBody StudentDto studentDto){
        studentService.updateUser(studentDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable(value = "id")UUID uuid){
        studentService.deleteUser(uuid);
    }
}
