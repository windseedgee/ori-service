package com.example.oriservice.resource;


import com.example.oriservice.dto.HeroDto;
import com.example.oriservice.dto.StudentDto;
import com.example.oriservice.entity.Hero;
import com.example.oriservice.entity.Student;
import com.example.oriservice.service.HeroService;
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
@RequestMapping(value = "/hero")
public class HeroResource {

    private final HeroService heroService;

    @GetMapping("/{id}")
    public Hero getUserById(@PathVariable(value = "id")UUID uuid){
        return heroService.getUserById(uuid);
    }

    @PostMapping
    public void createUser(@RequestBody HeroDto heroDto){
        heroService.create(heroDto);
    }

    @PutMapping
    public void updateUser(@RequestBody HeroDto heroDto){
        heroService.updateUser(heroDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable(value = "id")UUID uuid){
        heroService.deleteUser(uuid);
    }
}
