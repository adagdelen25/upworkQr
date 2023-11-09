package com.upwork.hometask.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

  @Before("execution(* com.upwork.hometask.demo.services.qrCode.CheckInService.checkIn())")
  public void getAllAdvice(JoinPoint joinPoint){
    log.debug("service method");
  }

}
