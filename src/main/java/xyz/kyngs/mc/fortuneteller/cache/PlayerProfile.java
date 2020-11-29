package xyz.kyngs.mc.fortuneteller.cache;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlayerProfile {

    private final String name;
    private LocalDate lastResetDate;

    public String getName() {
        return name;
    }

    public int getRound() {
        return round;
    }

    public void setLastResetDate(LocalDate lastResetDate) {
        this.lastResetDate = lastResetDate;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public PlayerProfile(String name, LocalDate lastResetDate, int round) {
        this.name = name;
        this.lastResetDate = lastResetDate;
        this.round = round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    private int round;

}
