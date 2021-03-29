package content.service.scheduler;

import java.util.Calendar;

import org.quartz.impl.calendar.BaseCalendar;

public class DailyCalendar extends BaseCalendar {
    private static final String invalidHourOfDay = "Invalid hour of day: ";
    private static final String invalidMinute = "Invalid minute: ";
    private static final String invalidSecond = "Invalid second: ";
    private static final String invalidMillis = "Invalid millis: ";
    private static final String invalidExcludedTimeRange = "Invalid excluded time range: ";
    private static final String separator = " - ";
    private static final long oneMillis = 1;
    public static final String colon = ":";

    private String name;
    private int excludedStartingHourOfDay;
    private int excludedStartingMinute;
    private int excludedStartingSecond;
    private int excludedStartingMillis;
    private int excludedEndingHourOfDay;
    private int excludedEndingMinute;
    private int excludedEndingSecond;
    private int excludedEndingMillis;

    public DailyCalendar(String name,
                         String excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern,
                         String excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern) {
        super();
        this.name = name;
        validate(excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern,
                 excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern);
    }

    public DailyCalendar(String name,
                         org.quartz.Calendar calendar,
                         String excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern,
                         String excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern) {
        super(calendar);
        this.name = name;
        validate(excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern,
                 excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern);
    }

    public DailyCalendar(String name,
                         int excludedStartingHourOfDay,
                         int excludedStartingMinute,
                         int excludedStartingSecond,
                         int excludedStartingMillis,
                         int excludedEndingHourOfDay,
                         int excludedEndingMinute,
                         int excludedEndingSecond,
                         int excludedEndingMillis) {
        super();
        this.name = name;
        validate(excludedStartingHourOfDay,
                 excludedStartingMinute,
                 excludedStartingSecond,
                 excludedStartingMillis,
                 excludedEndingHourOfDay,
                 excludedEndingMinute,
                 excludedEndingSecond,
                 excludedEndingMillis);
    }
    
    public DailyCalendar(String name,
                         org.quartz.Calendar calendar,
                         int excludedStartingHourOfDay,
                         int excludedStartingMinute,
                         int excludedStartingSecond,
                         int excludedStartingMillis,
                         int excludedEndingHourOfDay,
                         int excludedEndingMinute,
                         int excludedEndingSecond,
                         int excludedEndingMillis) {
        super(calendar);
        this.name = name;
        validate(excludedStartingHourOfDay,
                 excludedStartingMinute,
                 excludedStartingSecond,
                 excludedStartingMillis,
                 excludedEndingHourOfDay,
                 excludedEndingMinute,
                 excludedEndingSecond,
                 excludedEndingMillis);
    }

    public DailyCalendar(String name,
                         Calendar excludedStartingCalendar,
                         Calendar excludedEndingCalendar) {
        super();
        this.name = name;
        validate(excludedStartingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedStartingCalendar.get(Calendar.MINUTE),
                 excludedStartingCalendar.get(Calendar.SECOND),
                 excludedStartingCalendar.get(Calendar.MILLISECOND),
                 excludedEndingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedEndingCalendar.get(Calendar.MINUTE),
                 excludedEndingCalendar.get(Calendar.SECOND),
                 excludedEndingCalendar.get(Calendar.MILLISECOND));

    }

    public DailyCalendar(String name,
                         org.quartz.Calendar calendar,
                         Calendar excludedStartingCalendar,
                         Calendar excludedEndingCalendar) {
        super(calendar);
        this.name = name;
        validate(excludedStartingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedStartingCalendar.get(Calendar.MINUTE),
                 excludedStartingCalendar.get(Calendar.SECOND),
                 excludedStartingCalendar.get(Calendar.MILLISECOND),
                 excludedEndingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedEndingCalendar.get(Calendar.MINUTE),
                 excludedEndingCalendar.get(Calendar.SECOND),
                 excludedEndingCalendar.get(Calendar.MILLISECOND));
    }

    public DailyCalendar(String name,
                         long excludedStartingTimeInMillis,
                         long excludedEndingTimeInMillis) {
        super();
        this.name = name;
        Calendar excludedStartingCalendar = Calendar.getInstance();
        excludedStartingCalendar.setTimeInMillis(excludedStartingTimeInMillis);
        Calendar excludedEndingCalendar = Calendar.getInstance();
        excludedEndingCalendar.setTimeInMillis(excludedEndingTimeInMillis);
        validate(excludedStartingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedStartingCalendar.get(Calendar.MINUTE),
                 excludedStartingCalendar.get(Calendar.SECOND),
                 excludedStartingCalendar.get(Calendar.MILLISECOND),
                 excludedEndingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedEndingCalendar.get(Calendar.MINUTE),
                 excludedEndingCalendar.get(Calendar.SECOND),
                 excludedEndingCalendar.get(Calendar.MILLISECOND));
    }

    public DailyCalendar(String name,
                         org.quartz.Calendar calendar,
                         long excludedStartingTimeInMillis,
                         long excludedEndingTimeInMillis) {
        super(calendar);
        this.name = name;
        Calendar excludedStartingCalendar = Calendar.getInstance();
        excludedStartingCalendar.setTimeInMillis(excludedStartingTimeInMillis);
        Calendar excludedEndingCalendar = Calendar.getInstance();
        excludedEndingCalendar.setTimeInMillis(excludedEndingTimeInMillis);
        validate(excludedStartingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedStartingCalendar.get(Calendar.MINUTE),
                 excludedStartingCalendar.get(Calendar.SECOND),
                 excludedStartingCalendar.get(Calendar.MILLISECOND),
                 excludedEndingCalendar.get(Calendar.HOUR_OF_DAY),
                 excludedEndingCalendar.get(Calendar.MINUTE),
                 excludedEndingCalendar.get(Calendar.SECOND),
                 excludedEndingCalendar.get(Calendar.MILLISECOND));
    }

    public String getName() {
        return name;
    }

    public int getExcludedStartingHourOfDay() {
        return excludedStartingHourOfDay;
    }

    public int getExcludedStartingMinute() {
        return excludedStartingMinute;
    }

    public int getExcludedStartingSecond() {
        return excludedStartingSecond;
    }

    public int getExcludedStartingMillis() {
        return excludedStartingMillis;
    }

    public int getExcludedEndingHourOfDay() {
        return excludedEndingHourOfDay;
    }

    public int getExcludedEndingMinute() {
        return excludedEndingMinute;
    }

    public int getExcludedEndingSecond() {
        return excludedEndingSecond;
    }

    public int getExcludedEndingMillis() {
        return excludedEndingMillis;
    }

    @Override
    public boolean isTimeIncluded(long timeInMillis) {
        // System.out.println("********* daily calendar isTimeIncluded *********");
        boolean isTimeIncluded = false;
        // if (super.isTimeIncluded(timeInMillis)) {
            long startOfDayInMillis = getStartOfDayInMillis(timeInMillis);
            long excludedStartingTimeInMillis = getExcludedStartingTimeInMillis(timeInMillis);
            long excludedEndingTimeInMillis = getExcludedEndingTimeInMillis(timeInMillis);
            long endOfDayInMillis = getEndOfDayInMillis(timeInMillis);
            // System.out.println("startOfDayInMillis: " + startOfDayInMillis);
            // System.out.println("excludedStartingTimeInMillis: " + excludedStartingTimeInMillis);
            // System.out.println("excludedEndingTimeInMillis: " + excludedEndingTimeInMillis);
            // System.out.println("endOfDayInMillis: " + endOfDayInMillis);
            if ((timeInMillis > startOfDayInMillis && timeInMillis < excludedStartingTimeInMillis) ||
                (timeInMillis > excludedEndingTimeInMillis && timeInMillis < endOfDayInMillis)) {
                isTimeIncluded = true;
            }
        // }
        // System.out.println("isTimeIncluded: " + isTimeIncluded + separator + timeInMillis);
        return isTimeIncluded;
    }

    @Override
    public long getNextIncludedTime(long timeInMillis) {
        long nextIncludedTime = timeInMillis + oneMillis;
        if (!isTimeIncluded(nextIncludedTime)) {
            nextIncludedTime = getExcludedEndingTimeInMillis(timeInMillis) + oneMillis;
        }
        // System.out.println("********* daily calendar getNextIncludedTime: " + nextIncludedTime + "*********");
        return nextIncludedTime;
    }

    public long getStartOfDayInMillis(long timeInMillis) {
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTimeInMillis(timeInMillis);
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);
        return startOfDay.getTimeInMillis();
    }

    public long getEndOfDayInMillis(long timeInMillis) {
        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTimeInMillis(timeInMillis);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);
        return endOfDay.getTimeInMillis();
    }

    public long getExcludedStartingTimeInMillis(long timeInMillis) {
        Calendar excludedStartingTime = Calendar.getInstance();
        excludedStartingTime.setTimeInMillis(timeInMillis);
        excludedStartingTime.set(Calendar.HOUR_OF_DAY, excludedStartingHourOfDay);
        excludedStartingTime.set(Calendar.MINUTE, excludedStartingMinute);
        excludedStartingTime.set(Calendar.SECOND, excludedStartingSecond);
        excludedStartingTime.set(Calendar.MILLISECOND, excludedStartingMillis);
        return excludedStartingTime.getTimeInMillis();
    }

    public long getExcludedEndingTimeInMillis(long timeInMillis) {
        Calendar excludedEndingTime = Calendar.getInstance();
        excludedEndingTime.setTimeInMillis(timeInMillis);
        excludedEndingTime.set(Calendar.HOUR_OF_DAY, excludedEndingHourOfDay);
        excludedEndingTime.set(Calendar.MINUTE, excludedEndingMinute);
        excludedEndingTime.set(Calendar.SECOND, excludedEndingSecond);
        excludedEndingTime.set(Calendar.MILLISECOND, excludedEndingMillis);
        return excludedEndingTime.getTimeInMillis();
    }

    public Calendar getExcludedStartingCalendar() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(getExcludedStartingTimeInMillis(today.getTimeInMillis()));
        return today;
    }

    public Calendar getExcludedEndingCalendar() {
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(getExcludedEndingTimeInMillis(today.getTimeInMillis()));
        return today;
    }

    public void validate(String excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern,
                         String excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern) {
        String [] excludedStartingTime = excludedStartingHourOfDayMinuteSecondMillisColonDelimittedPattern.split(colon);
        int excludedStartingHourOfDay = Integer.parseInt(excludedStartingTime[0]);
        int excludedStartingMinute = Integer.parseInt(excludedStartingTime[1]);
        int excludedStartingSecond = Integer.parseInt(excludedStartingTime[2]);
        int excludedStartingMillis = Integer.parseInt(excludedStartingTime[3]);
        String [] excludedEndingTime = excludedEndingHourOfDayMinuteSecondMillisColonDelimittedPattern.split(colon);
        int excludedEndingHourOfDay = Integer.parseInt(excludedEndingTime[0]);
        int excludedEndingMinute = Integer.parseInt(excludedEndingTime[1]);
        int excludedEndingSecond = Integer.parseInt(excludedEndingTime[2]);
        int excludedEndingMillis = Integer.parseInt(excludedEndingTime[3]);
        validate(excludedStartingHourOfDay,
                 excludedStartingMinute,
                 excludedStartingSecond,
                 excludedStartingMillis,
                 excludedEndingHourOfDay,
                 excludedEndingMinute,
                 excludedEndingSecond,
                 excludedEndingMillis);
    }

    public void validate(int excludedStartingHourOfDay,
                         int excludedStartingMinute,
                         int excludedStartingSecond,
                         int excludedStartingMillis,
                         int excludedEndingHourOfDay,
                         int excludedEndingMinute,
                         int excludedEndingSecond,
                         int excludedEndingMillis) {
        this.excludedStartingHourOfDay = excludedStartingHourOfDay;
        this.excludedStartingMinute = excludedStartingMinute;
        this.excludedStartingSecond = excludedStartingSecond;
        this.excludedStartingMillis = excludedStartingMillis;
        this.excludedEndingHourOfDay = excludedEndingHourOfDay;
        this.excludedEndingMinute = excludedEndingMinute;
        this.excludedEndingSecond = excludedEndingSecond;
        this.excludedEndingMillis = excludedEndingMillis;
        validate(excludedStartingHourOfDay,
                 excludedStartingMinute,
                 excludedStartingSecond,
                 excludedStartingMillis);
        validate(excludedEndingHourOfDay,
                 excludedEndingMinute,
                 excludedEndingSecond,
                 excludedEndingMillis);
        long today = Calendar.getInstance().getTimeInMillis();
        long excludedEndingTime = getExcludedEndingTimeInMillis(today);
        long excludedStartingTime = getExcludedStartingTimeInMillis(today);
        if (excludedStartingTime >= excludedEndingTime) {
            throw new IllegalArgumentException(invalidExcludedTimeRange +
                                               excludedStartingTime +
                                               separator +
                                               excludedEndingTime);
        }
    }
    
    public void validate(int hourOfDay,
                         int minute,
                         int second,
                         int millis) {
        if (hourOfDay < 0 || hourOfDay > 23) {
            throw new IllegalArgumentException(invalidHourOfDay + hourOfDay);
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException(invalidMinute + minute);
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException(invalidSecond + second);
        }
        if (millis < 0 || millis > 999) {
            throw new IllegalArgumentException(invalidMillis + millis);
        }
    }

    public String toString() {
        long todayInMillis = Calendar.getInstance().getTimeInMillis();
        StringBuilder builder = new StringBuilder();
        builder.append("base calendar: ");
        builder.append(super.toString());
        builder.append("\ndaily calendar: ");
        builder.append("\nexcluded start of day in millis: ");
        builder.append(getStartOfDayInMillis(todayInMillis));
        builder.append("\nexcluded starting time: ");
        builder.append(getExcludedStartingTimeInMillis(todayInMillis));
        builder.append("\nexcluded ending time: ");
        builder.append(getExcludedEndingTimeInMillis(todayInMillis));
        builder.append("\nexcluded end of day in millis: ");
        builder.append(getEndOfDayInMillis(todayInMillis));
        return builder.toString();
    }
}