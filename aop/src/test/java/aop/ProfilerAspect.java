package aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import aop.profiler.Profiler;

@Aspect
@Configurable
public class ProfilerAspect {
    @Autowired private Profiler profiler;

    public ProfilerAspect() {
    }

    @Pointcut("execution(* aop.AopTarget.profile(String))")
    public void profilerPointcut() {
    }

    @Around("profilerPointcut()")
    public Object profile(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }
}