package com.example.oriservice.aspect;

import com.example.oriservice.dto.HeroDto;
import com.example.oriservice.entity.Hero;
import com.example.oriservice.service.HeroServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class HeroAspect {

    @Around(value = "execution(* com.example.oriservice.service.HeroService.create(..))")
    public Object addLogInfo(ProceedingJoinPoint pjp) throws Throwable {
        var args = pjp.getArgs();
        log.info("@Around com.example.oriservice.service.HeroService.create args:{}",args);
        return pjp.proceed();
    }

    @Before(value = "execution(* com.example.oriservice.service.HeroService.getUserById(..))")
    public void printInfo(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        log.info("@Before com.example.oriservice.service.HeroService.getUserById :{}",signature);
    }

    @AfterReturning(value = "execution(* com.example.oriservice.service.HeroService.getUserById(..))",returning = "returned")
    public Object printInfoReturn(JoinPoint joinPoint, Object returned){
        Signature signature = joinPoint.getSignature();
        if(returned instanceof Hero){
            Hero hero = (Hero) returned;
            hero.setName("test AfterReturning");
            log.info("@AfterReturning com.example.oriservice.service.HeroService.getUserById :{}",signature);
            return returned;
        }
        log.info("@AfterReturning result:{}",returned);
        return returned;

    }

}
