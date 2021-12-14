import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeMachineClock extends Clock {

    private long offset;

    public void addMinutes(long minutes) {
        offset += minutes;
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return Instant.now().plus(offset, ChronoUnit.MINUTES);
    }
}
