package homeschool.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import homeschool.profiler.Profiler;

@Aspect
@Configurable
public class TestAspect {
    @Autowired
    private Profiler profiler;

    public TestAspect() {
    }

    @Pointcut("execution(* homeschool.test.StudentServiceTest.new*(..))")
    public void newPointcut() {
    }

    @Pointcut("execution(* homeschool.test.StudentServiceTest.add*(..))")
    public void addPointcut() {
    }

    @Around("newPointcut()")
    public Object profileNew(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }

    @Around("addPointcut()")
    public Object profileAdd(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }
}