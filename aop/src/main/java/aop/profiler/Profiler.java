package aop.profiler;

import org.aspectj.lang.ProceedingJoinPoint;

public interface Profiler {
    public Object profile(ProceedingJoinPoint joinPoint);
    public long getSuccessCount();
    public double getAverageSuccessElapsedTimeInMillis();
    public long getFailureCount();
    public double getAverageFailureElapsedTimeInMillis();
    public void reset();
    public boolean isEnabled();
    public void setEnabled(boolean enabled);
}