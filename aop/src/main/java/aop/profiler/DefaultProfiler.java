package aop.profiler;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class DefaultProfiler implements Profiler {
    private static final Logger logger = Logger.getLogger(DefaultProfiler.class);

    private boolean enabled;
    private long successCount;
    private double totalSuccessElapsedTimeInMillis;
    private double averageSuccessElapsedTimeInMillis;
    private long failureCount;
    private double totalFailureElapsedTimeInMillis;
    private double averageFailureElapsedTimeInMillis;

    public DefaultProfiler() {
        enabled = true;
        successCount = 0;
        totalSuccessElapsedTimeInMillis = 0.0;
        averageSuccessElapsedTimeInMillis = 0.0;
        failureCount = 0;
        totalFailureElapsedTimeInMillis = 0.0;
        averageFailureElapsedTimeInMillis = 0.0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Object profile(ProceedingJoinPoint joinPoint) {
        Object returnValue;
        if (isEnabled()) {
            String methodProfile = joinPoint.toLongString();
            double startTimeInMillis = System.currentTimeMillis();
            double stopTimeInMillis;
            double elapsedTimeInMillis;
            try {
                debug("*** before: " + methodProfile);
                returnValue = joinPoint.proceed();
                stopTimeInMillis = System.currentTimeMillis();
                elapsedTimeInMillis = calculateSuccess(startTimeInMillis, stopTimeInMillis);
                debug("*** success elapsed time: " + elapsedTimeInMillis + "ms" + " : " + (elapsedTimeInMillis / 1000) + "s");
                debug("*** after: " + methodProfile);
            } catch (Throwable t) {
                logger.error(t);
                stopTimeInMillis = System.currentTimeMillis();
                elapsedTimeInMillis = calculateFailure(startTimeInMillis, stopTimeInMillis);
                logger.warn("*** failure elapsed time: " + elapsedTimeInMillis + "ms" + " : " + (elapsedTimeInMillis / 1000) + "s");
                debug("*** after: " + methodProfile);
                throw new RuntimeException(t);
            }
        } else {
            try {
                returnValue = joinPoint.proceed();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return returnValue;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public double getAverageSuccessElapsedTimeInMillis() {
        return averageSuccessElapsedTimeInMillis;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public double getAverageFailureElapsedTimeInMillis() {
        return averageFailureElapsedTimeInMillis;
    }

    public synchronized double calculateSuccess(final double startTimeInMillis, final double stopTimeInMillis) {
        double elapsedTimeInMillis = stopTimeInMillis - startTimeInMillis;
        totalSuccessElapsedTimeInMillis += elapsedTimeInMillis;
        averageSuccessElapsedTimeInMillis = totalSuccessElapsedTimeInMillis / ++successCount;
        return elapsedTimeInMillis;
    }

    public synchronized double calculateFailure(final double startTimeInMillis, final double stopTimeInMillis) {
        double elapsedTimeInMillis = stopTimeInMillis - startTimeInMillis;
        totalFailureElapsedTimeInMillis += elapsedTimeInMillis;
        averageFailureElapsedTimeInMillis = totalFailureElapsedTimeInMillis / ++failureCount;
        return elapsedTimeInMillis;
    }

    public synchronized void reset() {
        successCount = 0;
        totalSuccessElapsedTimeInMillis = 0.0;
        averageSuccessElapsedTimeInMillis = 0.0;
        failureCount = 0;
        totalFailureElapsedTimeInMillis = 0.0;
        averageFailureElapsedTimeInMillis = 0.0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Profiler:");
        builder.append("\nsuccess count: ");
        builder.append(successCount);
        builder.append(" : average elapsed time (ms): ");
        builder.append(averageSuccessElapsedTimeInMillis);
        builder.append(" : total elapsed time (ms): ");
        builder.append(totalSuccessElapsedTimeInMillis);
        builder.append("\nfailure count: ");
        builder.append(failureCount);
        builder.append(" : average elapsed time (ms): ");
        builder.append(averageFailureElapsedTimeInMillis);
        builder.append(" : total elapsed time (ms): ");
        builder.append(totalFailureElapsedTimeInMillis);
        return builder.toString();
    }

    private void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}