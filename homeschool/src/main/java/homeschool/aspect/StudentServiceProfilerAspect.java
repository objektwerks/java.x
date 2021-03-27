package homeschool.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import homeschool.profiler.Profiler;

@Aspect
@Configurable
public class StudentServiceProfilerAspect {
    @Autowired
    private Profiler profiler;

    public StudentServiceProfilerAspect() {
    }

    @Pointcut("execution(* homeschool.service.StudentService.saveTeacher(..))")
    public void savePointcut() {
    }

    @Pointcut("execution(* homeschool.service.StudentService.addGradeToStudent(..))")
    public void addPointcut() {
    }

    @Pointcut("execution(* homeschool.service.StudentService.list*(..))")
    public void listPointcut() {
    }

    @Pointcut("execution(* homeschool.service.StudentService.find*(..))")
    public void findPointcut() {
    }
    @Around("savePointcut()")
    public Object profileSave(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }

    @Around("addPointcut()")
    public Object profileAdd(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }

    @Around("listPointcut()")
    public Object profileList(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }

    @Around("findPointcut()")
    public Object profileFind(ProceedingJoinPoint joinPoint) {
        return profiler.profile(joinPoint);
    }

}