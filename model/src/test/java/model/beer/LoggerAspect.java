package model.beer;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggerAspect {
    private static final Logger logger = Logger.getLogger(LoggerAspect.class);

    public LoggerAspect() {
    }

    @Pointcut("execution(* model.beer.*.*(..))")
    private void loggerPointcut() {
    }

    @Before("loggerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.debug("*** before: " + joinPoint.getSignature().getName());
    }

    @After("loggerPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        logger.debug("*** after: " + joinPoint.getSignature().getName());
    }
}