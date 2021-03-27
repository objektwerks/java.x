package persistence.hibernate;

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

    @Pointcut("execution(* persistence.hibernate.HibernateGenericEntityManager.*(..))")
    private void loggerPointcut() {
    }

    @Before("loggerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        logger.trace("*** before: " + joinPoint.getSignature().getName());
    }

    @After("loggerPointcut()")
    public void logAfter(JoinPoint joinPoint) {
        logger.trace("*** after: " + joinPoint.getSignature().getName());
    }
}