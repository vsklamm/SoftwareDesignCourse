package ru.vsklamm.statistics;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kinda optimized (no) for event registration since there are always more events
 * than requests for stats in any system.
 */
public class EventsStatisticImpl implements EventsStatistic {
    private static final long MINUTES_PER_HOUR = Duration.ofHours(1).toMinutes();

    private final Map<String, List<Instant>> events = new HashMap<>();
    private final Clock clock;

    public EventsStatisticImpl(final Clock clock) {
        this.clock = clock;
    }

    @Override
    public void incEvent(final String name) {
        if (!events.containsKey(name)) {
            events.put(name, new ArrayList<>());
        }
        events.get(name).add(clock.instant());
    }

    @Override
    public double getEventStatisticByName(final String name) {
        dropOldStats();
        if (!events.containsKey(name)) {
            return 0.0;
        }
        return countRpm(events.get(name).size());
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        dropOldStats();
        Map<String, Double> stats = new HashMap<>();
        events.forEach((name, instants) -> stats.put(name, countRpm(instants.size())));
        return stats;
    }

    @Override
    public void printStatistic() {
        final var stats = getAllEventStatistic();
        final var header = "| %32s | %12s |".formatted("Event name", "RPM");
        System.out.println("-".repeat(header.length()));
        System.out.println(header);
        System.out.println("-".repeat(header.length()));
        stats.forEach((name, rpm) -> System.out.printf("| %32s | %12.3f |%n", name, rpm));
    }

    private void dropOldStats() {
        final var hourAgo = clock.instant().minus(1L, ChronoUnit.HOURS);
        events.replaceAll((n, instants) -> instants.stream().dropWhile(e -> e.isBefore(hourAgo)).toList());
        events.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public double countRpm(final int rph) {
        return (double) rph / MINUTES_PER_HOUR;
    }
}