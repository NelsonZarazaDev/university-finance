package com.universityfinance.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class StudentOptionCacheAspect {

    private final CacheManager cacheManager;

    @AfterReturning(
        "execution(* com.universityfinance.web.rest.StudentResource.createStudent(..)) || " +
            "execution(* com.universityfinance.web.rest.StudentResource.updateStudent(..)) || " +
            "execution(* com.universityfinance.web.rest.StudentResource.partialUpdateStudent(..)) || " +
            "execution(* com.universityfinance.web.rest.StudentResource.deleteStudent(..))"
    )
    public void invalidateStudentOptions() {
        Cache cache = cacheManager.getCache("studentOptions");

        if (cache != null) {
            cache.clear();
            log.info("studentOptions cache invalidated");
        }
    }
}
