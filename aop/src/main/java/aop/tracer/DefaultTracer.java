package aop.tracer;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class DefaultTracer implements Tracer {
    private static final Logger logger = Logger.getLogger(DefaultTracer.class);

    public void before(JoinPoint joinPoint) {
        logger.trace("*** before: " + joinPoint.toLongString());
    }

    public void after(JoinPoint joinPoint) {
        logger.trace("*** after: " + joinPoint.toLongString());
    }
}