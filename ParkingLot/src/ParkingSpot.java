/**
 * The {@code ParkingSpot} class represents a single parking spot in the parking lot.
 * <p>
 * Each parking spot can either be free or occupied by a {@link Car}. This class provides methods
 * to check occupancy, park a car, or free the spot.
 * <p>
 * <b>Usage Example:</b>
 * <pre>
 *     ParkingSpot spot = new ParkingSpot();
 *     if (!spot.isOccupied()) {
 *         spot.parkCar(new Car("123-45-678"));
 *     }
 *     // ...
 *     spot.removeCar();
 * </pre>
 */
public class ParkingSpot {
    /**
     * Indicates whether the spot is currently occupied.
     */
    private boolean occupied;

    /**
     * The car currently parked in this spot, or {@code null} if the spot is free.
     */
    private Car parkedCar;

    /**
     * Constructs a new, unoccupied parking spot.
     */
    public ParkingSpot() {
        this.occupied = false;
        this.parkedCar = null;
    }

    /**
     * Returns whether the spot is currently occupied.
     *
     * @return {@code true} if occupied; {@code false} otherwise
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Returns the car currently parked in this spot, or {@code null} if the spot is free.
     *
     * @return the parked {@link Car}, or {@code null} if not occupied
     */
    public Car getParkedCar() {
        return parkedCar;
    }

    /**
     * Parks the specified car in this spot and marks it as occupied.
     *
     * @param car the car to park
     */
    public void parkCar(Car car) {
        this.parkedCar = car;
        this.occupied = true;
    }

    /**
     * Frees this spot and removes the parked car (if any).
     */
    public void removeCar() {
        this.parkedCar = null;
        this.occupied = false;
    }
}