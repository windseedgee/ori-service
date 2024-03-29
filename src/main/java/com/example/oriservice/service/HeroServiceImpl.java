package com.example.oriservice.service;

import com.example.oriservice.dto.HeroDto;
import com.example.oriservice.entity.Hero;
import com.example.oriservice.repository.HeroRepository;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;

    @Override
//    @Transactional
    public void create(HeroDto user) {
        log.info("test Transactional");
        createUser(user);
//        throw new RuntimeException("Forced rollback to verify @Transactional");
    }

    @Override
    public void updateUser(HeroDto user) {
        Hero updateHero = heroRepository.findById(user.getId()).get();
        updateHero.setName(user.getName());
        heroRepository.save(updateHero);
    }

    @Override
    public Hero getUserById(UUID id) {
        log.info("first");
//        heroRepository.findById(id);
/*        for(int i = 1;i <= 1000; i++){
            int finalI = i;
            Thread.startVirtualThread(()-> log.info("mock get user virtual thread:{},number:{}",Thread.currentThread(), finalI));
        }
        log.info("mock get user thread:{}",Thread.currentThread());*/
        return heroRepository.findById(id).get();
    }

    @Override
    public void deleteUser(UUID uuid) {
        heroRepository.deleteById(uuid);
    }

    private void createUser(HeroDto user){
        Hero newStudent = new Hero();
        newStudent.setName(user.getName());
        newStudent.setAutoRenewal(user.getAutoRenewal());
        heroRepository.save(newStudent);
    }
}
