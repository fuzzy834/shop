package ua.home.stat_shop.persistence.tasks;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Scheduler {

    private static final ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);

    public static void scheduleTask(Runnable runnable, LocalDateTime executionTime) {
        ZonedDateTime zonedDateTime = executionTime.atZone(ZoneId.systemDefault());
        Long millis = zonedDateTime.toInstant().toEpochMilli();
        scheduler.schedule(runnable, new Date(millis));
    }
}
