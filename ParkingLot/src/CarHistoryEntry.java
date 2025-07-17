/**
 * The {@code CarHistoryEntry} class represents a record of a single parking event in the parking lot system.
 * <p>
 * Each entry contains a reference to the {@link Car} that was parked, the entry and exit times (in milliseconds since epoch),
 * and the amount paid for the parking session.
 * <p>
 * This class is typically used to store historical parking data for reporting, auditing, and statistics.
 *<p>
 * Example usage:
 * <pre>
 *     Car car = new Car("123-45-678");
 *     long entryTime = System.currentTimeMillis();
 *     // ... after parking ...
 *     long exitTime = System.currentTimeMillis();
 *     double paid = 25.0;
 *     CarHistoryEntry entry = new CarHistoryEntry(car, entryTime, exitTime, paid);
 * </pre>
 */
public class CarHistoryEntry {
    /**
     * The car involved in this parking session.
     */
    private final Car car;

    /**
     * The entry time in milliseconds since epoch.
     */
    private final long entryTime;

    /**
     * The exit time in milliseconds since epoch.
     */
    private final long exitTime;

    /**
     * The amount paid for this parking session.
     */
    private final double paid;

    /**
     * Constructs a new {@code CarHistoryEntry} with the specified car, entry/exit times, and payment.
     *
     * @param car       the car that was parked
     * @param entryTime the time (in ms since epoch) the car entered the parking lot
     * @param exitTime  the time (in ms since epoch) the car exited the parking lot
     * @param paid      the amount paid for this parking session
     */
    public CarHistoryEntry(Car car, long entryTime, long exitTime, double paid) {
        this.car = car;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.paid = paid;
    }

    /**
     * Returns the {@link Car} associated with this history entry.
     *
     * @return the parked car
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns the entry time (in ms since epoch) for this parking session.
     *
     * @return entry time in milliseconds
     */
    public long getEntryTime() {
        return entryTime;
    }

    /**
     * Returns the exit time (in ms since epoch) for this parking session.
     *
     * @return exit time in milliseconds
     */
    public long getExitTime() {
        return exitTime;
    }

    /**
     * Returns the amount paid for this parking session.
     *
     * @return amount paid
     */
    public double getPaid() {
        return paid;
    }
}