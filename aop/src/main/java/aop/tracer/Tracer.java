package aop.tracer;

import org.aspectj.lang.JoinPoint;

public interface Tracer {
    public void before(JoinPoint joinPoint);
    public void after(JoinPoint joinPoint);
}