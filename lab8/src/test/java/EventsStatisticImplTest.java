import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.vsklamm.statistics.EventsStatistic;
import ru.vsklamm.statistics.EventsStatisticImpl;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class EventsStatisticImplTest {
    private static final long MINUTES_PER_HOUR = Duration.ofHours(1L).toMinutes();

    private TimeMachineClock clock;
    private EventsStatistic eventsStatistic;

    @BeforeEach
    void setup() {
        clock = new TimeMachineClock();
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    @Test
    void testGetEventStatisticsNonExistingName() {
        assertThat(eventsStatistic.getEventStatisticByName("Event #None")).isZero();
    }

    @Test
    void testGetEventStatisticByNameOne() {
        eventsStatistic.incEvent("Event #1");
        assertThat(eventsStatistic.getEventStatisticByName("Event #1")).isEqualTo(countRpm(1));
    }

    @Test
    void testGetEventStatisticByNameSome() {
        eventsStatistic.incEvent("Event #1");
        eventsStatistic.incEvent("Event #2");
        eventsStatistic.incEvent("Event #2");
        eventsStatistic.incEvent("Event #3");
        assertThat(eventsStatistic.getEventStatisticByName("Event #1")).isEqualTo(countRpm(1));
        assertThat(eventsStatistic.getEventStatisticByName("Event #2")).isEqualTo(countRpm(2));
        assertThat(eventsStatistic.getEventStatisticByName("Event #3")).isEqualTo(countRpm(1));
    }

    @Test
    void testGetEventStatisticByNameOldEvent() {
        eventsStatistic.incEvent("Event #1");
        clock.addMinutes(60L);
        eventsStatistic.printStatistic();
        clock.addMinutes(1L);
        eventsStatistic.printStatistic();
        assertThat(eventsStatistic.getEventStatisticByName("Event #1")).isZero();
    }

    @Test
    void testGetEventStatisticByNameAfterHour() {
        eventsStatistic.incEvent("Event #1");
        eventsStatistic.incEvent("Event #2");
        clock.addMinutes(61L);
        eventsStatistic.incEvent("Event #1");
        assertThat(eventsStatistic.getEventStatisticByName("Event #1")).isEqualTo(countRpm(1));
        assertThat(eventsStatistic.getEventStatisticByName("Event #2")).isZero();
    }

    @Test
    void testAllEventStatisticSimple() {
        eventsStatistic.incEvent("Event #1");
        eventsStatistic.incEvent("Event #2");

        clock.addMinutes(20L);

        eventsStatistic.incEvent("Event #1");
        eventsStatistic.incEvent("Event #2");

        clock.addMinutes(20L);

        eventsStatistic.incEvent("Event #2");
        eventsStatistic.incEvent("Event #2");

        var allEventStatistic = eventsStatistic.getAllEventStatistic();
        assertThat(allEventStatistic).containsEntry("Event #1", countRpm(2));
        assertThat(allEventStatistic).containsEntry("Event #2", countRpm(4));
        assertThat(allEventStatistic).doesNotContainKey("Event #3");

        eventsStatistic.printStatistic();

        clock.addMinutes(21L);

        eventsStatistic.incEvent("Event #3");
        eventsStatistic.incEvent("Event #3");

        allEventStatistic = eventsStatistic.getAllEventStatistic();
        assertThat(allEventStatistic).containsEntry("Event #1", countRpm(1));
        assertThat(allEventStatistic).containsEntry("Event #2", countRpm(3));
        assertThat(allEventStatistic).containsEntry("Event #3", countRpm(2));
    }

    private double countRpm(final int rph) {
        return (double) rph / MINUTES_PER_HOUR;
    }
}