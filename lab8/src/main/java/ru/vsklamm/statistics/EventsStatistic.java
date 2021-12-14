package ru.vsklamm.statistics;

import java.util.Map;

/**
 * Counts events occurring in the system.
 */
public interface EventsStatistic {
    /**
     * Register event.
     */
    void incEvent(final String name);

    /**
     * Get RPM (request per minute) of event by name.
     */
    double getEventStatisticByName(final String name);

    /**
     * Get RPMs (request per minute) of all events happened in system last hour.
     */
    Map<String, Double> getAllEventStatistic();

    /**
     * Print table with name and rpm of all events.
     */
    void printStatistic();
}
