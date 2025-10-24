package com.problems.meetingscheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reccurrance {
    private final RecurrenceType recurrenceType;
    private final LocalDateTime endTime;

    public Reccurrance(RecurrenceType recurrenceType, LocalDateTime endTime) {
        this.recurrenceType = recurrenceType;
        this.endTime = endTime;
    }

    public List<TimeInterval> generateOccurrences(TimeInterval baseInterval) {
        List<TimeInterval> occurrences = new ArrayList<>();
        LocalDateTime currentStart = baseInterval.getStartTime();
        LocalDateTime currentEnd = baseInterval.getEndTime();

        while (currentEnd.isBefore(endTime)) {
            TimeInterval timeInterval = new TimeInterval(currentStart, currentEnd);
            occurrences.add(timeInterval);

            switch (recurrenceType) {
                case MONTHLY:
                    currentStart = currentStart.plusMonths(1);
                    currentEnd = currentEnd.plusMonths(1);
                    break;
                case WEEKLY:
                    currentStart = currentStart.plusWeeks(1);
                    currentEnd = currentEnd.plusWeeks(1);
                    break;
                case DAILY:
                    currentStart = currentStart.plusDays(1);
                    currentEnd = currentEnd.plusDays(1);
                    break;
                default:
                    return occurrences;
            }
        }

        return occurrences;

    }
}
