package org.example.expert.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.expert.domain.common.annotation.LogAction;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.log.service.LogService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final LogService logService;

    @Around("@annotation(logAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, LogAction logAction) throws Throwable{
        AuthUser authUser = null;
        Long todoId = null;

        // 메서드 파라미터에서 AuthUser와 todoId 추출
        Object[] args = joinPoint.getArgs();
        for (Object arg : args){
            if (arg instanceof AuthUser){
                authUser = (AuthUser) arg;
            } else if (arg instanceof Long) {
                todoId = (Long) arg;
            }
        }

        // 로그 저장
        if (authUser != null && todoId != null){
            logService.saveLog(logAction.action(), authUser.getId(), todoId);
        }

        // 원래 메서드 실행
        return joinPoint.proceed();
    }
}
