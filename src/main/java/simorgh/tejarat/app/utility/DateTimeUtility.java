package simorgh.tejarat.app.utility;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DateTimeUtility {
    /**
     * it divides a period of time into small periods
     * @param startTime {@link LocalDateTime}
     * @param endTime {@link LocalDateTime}
     * @param interval {@link Integer}
     * @return LocalDateTime {@link LocalDateTime}
     * @throws IllegalArgumentException
     */
    public static List<LocalDateTime> dateTimeInterval(LocalDateTime startTime, LocalDateTime endTime, Integer interval)
    throws IllegalArgumentException {
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("end time cannot before start time");
        }

        List<LocalDateTime> timeSlots = new ArrayList<>();

        LocalDateTime currentTime = startTime;

        while (
            currentTime.isBefore(endTime) ||
            currentTime.equals(endTime) ||
            currentTime.plusMinutes(interval).isBefore(endTime) ||
            currentTime.plusMinutes(interval).equals(endTime)
        ) {
            timeSlots.add(currentTime);
            currentTime.plusMinutes(interval);
        }

        return timeSlots;
    }
}
