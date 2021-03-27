package homeschool.profiler;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Profiler {
    private Map<String, List<Long>> profiles = new HashMap<>();

    public Profiler() {
    }

    public Object profile(ProceedingJoinPoint joinPoint) {
        Object returnValue = null;
        String methodProfile = joinPoint.getSignature().toShortString();
        try {
            long startTime = System.nanoTime();
            returnValue = joinPoint.proceed();
            long stopTime = System.nanoTime();
            long elapsedTime = stopTime - startTime;
            if (!profiles.containsKey(methodProfile)) {
                profiles.put(methodProfile, new ArrayList<Long>());
            }
            profiles.get(methodProfile).add(elapsedTime);
        } catch (Throwable t) {
            profiles.get(methodProfile).add(-1L);
        }
        return returnValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        TimeUnit timeUnit = TimeUnit.NANOSECONDS;
        builder.append("Profiler:");
        for (Map.Entry<String, List<Long>> entry : profiles.entrySet()) {
            List<Long> elapsedTimes = entry.getValue();
            int transactionCount = elapsedTimes.size();
            if (transactionCount > 0) {
                long totalElapsedTime = 0;
                for (Long elapsedTime : elapsedTimes) {
                    totalElapsedTime = totalElapsedTime + elapsedTime;
                }
                long averageElapsedTime = totalElapsedTime / transactionCount;
                builder.append("\n");
                builder.append(entry.getKey());
                builder.append("\ttransaction count: ");
                builder.append(transactionCount);
                builder.append("\taverage elapsed time: ");
                builder.append(timeUnit.toMillis(averageElapsedTime));
                builder.append(" ms");
            }
        }
        return builder.toString();
    }
}