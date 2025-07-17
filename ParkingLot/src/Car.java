/**
 * The {@code Car} class represents a vehicle parked in the parking lot system.
 * It includes the license plate, entry and exit times, and supports cloning via the Prototype pattern.
 * <p>
 * Usage:
 * <ul>
 *     <li>Track when a car enters and exits the parking lot.</li>
 *     <li>Calculate parking duration.</li>
 *     <li>Clone the car object if needed.</li>
 * </ul>
 *
 * Example:
 * <pre>
 *     Car car = new Car("123-45-678");
 *     car.setEntryTime(System.currentTimeMillis());
 *     // ... later
 *     car.setExitTime(System.currentTimeMillis());
 *     long duration = car.getParkingDuration();
 * </pre>
 *
 * Implements {@link Cloneable} to support the Prototype design pattern.
 */
public class Car implements Cloneable {
    /**
     * The unique license plate of the car.
     */
    private final String licensePlate;

    /**
     * The entry time in milliseconds since epoch.
     */
    private long entryTime;

    /**
     * The exit time in milliseconds since epoch.
     */
    private long exitTime;

    /**
     * Creates a new car with the given license plate.
     * Entry and exit times are initialized to -1 (not set).
     *
     * @param licensePlate the license plate of the car
     */
    public Car(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = -1;
        this.exitTime = -1;
    }

    /**
     * Returns the license plate of the car.
     *
     * @return the license plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Returns the entry time in milliseconds since epoch.
     *
     * @return the entry time, or -1 if not set
     */
    public long getEntryTime() {
        return entryTime;
    }

    /**
     * Sets the entry time in milliseconds since epoch.
     *
     * @param entryTime the entry time
     */
    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    /**
     * Returns the exit time in milliseconds since epoch.
     *
     * @return the exit time, or -1 if not set
     */
    public long getExitTime() {
        return exitTime;
    }

    /**
     * Sets the exit time in milliseconds since epoch.
     *
     * @param exitTime the exit time
     */
    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Creates and returns a copy of this car.
     *
     * @return a clone of this car
     * @throws AssertionError if cloning is not supported
     */
    @Override
    public Car clone() {
        try {
            return (Car) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Returns a string representation of the car, including license plate, entry, and exit times.
     *
     * @return a string representation of the car
     */
    @Override
    public String toString() {
        return "Car{" +
                "licensePlate='" + licensePlate + '\'' +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                '}';
    }
}