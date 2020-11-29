package xyz.kyngs.mc.fortuneteller.utils;

import java.time.Duration;

public class DurationUtil {

    public static int toMinutePart(Duration duration){
        return (int) (duration.toMinutes() % 60);
    }

    public static int toSecondsPart(Duration duration){
        return (int) (duration.getSeconds() % 60);
    }

    public static int toHoursPart(Duration dur) {
        return (int) (dur.toHours() % 24);
    }
}
