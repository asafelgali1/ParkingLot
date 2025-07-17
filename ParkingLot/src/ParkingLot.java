import java.util.*;
/**
 * The {@code ParkingLot} class represents a parking lot with a fixed number of parking spots,
 * supports car entry and exit, maintains parking history, calculates statistics,
 * and notifies registered observers about changes.
 * <p>
 * This class uses the Singleton pattern to ensure only one parking lot instance exists.
 * It maintains a list of {@link ParkingSpot}s, a history of {@link CarHistoryEntry},
 * and notifies {@link ParkingLotObserver}s on state changes.
 * <p>
 * <b>Main Features:</b>
 * <ul>
 *     <li>Add and remove cars, preserving entry/exit times and calculating payment via a {@link PricingStrategy}.</li>
 *     <li>Keep a history of all parking sessions.</li>
 *     <li>Calculate statistics such as occupancy, average parking time, and daily revenue.</li>
 *     <li>Support observer pattern for real-time updates.</li>
 * </ul>
 *
 * <b>Usage Example:</b>
 * <pre>
 *     ParkingLot lot = ParkingLot.createLot(10, new HourlyPricingStrategy(10.0));
 *     lot.addCar(new Car("123-45-678"));
 *     // ... later ...
 *     lot.removeCar("123-45-678");
 *     double revenue = lot.getTodaysRevenue();
 * </pre>
 */
public class ParkingLot {
    /**
     * Singleton instance of the parking lot.
     */
    private static ParkingLot instance;

    /**
     * List of all parking spots in the lot.
     */
    private final List<ParkingSpot> spots;

    /**
     * List of observers to notify upon changes.
     */
    private final List<ParkingLotObserver> observers;

    /**
     * List of historical parking entries.
     */
    private final List<CarHistoryEntry> history;

    /**
     * Pricing strategy used for calculating parking fees.
     */
    private final PricingStrategy pricingStrategy;

    /**
     * Private constructor to enforce singleton pattern.
     *
     * @param totalSpots      total number of parking spots in the lot
     * @param pricingStrategy the pricing strategy to use
     */
    private ParkingLot(int totalSpots, PricingStrategy pricingStrategy) {
        this.spots = new ArrayList<>();
        for (int i = 1; i <= totalSpots; i++) {
            spots.add(new ParkingSpot());
        }
        this.observers = new ArrayList<>();
        this.history = new ArrayList<>();
        this.pricingStrategy = pricingStrategy;
    }

    /**
     * Creates or returns the singleton ParkingLot instance.
     *
     * @param totalSpots      total number of parking spots
     * @param pricingStrategy the pricing strategy for the lot
     * @return the singleton ParkingLot instance
     */
    public static ParkingLot createLot(int totalSpots, PricingStrategy pricingStrategy) {
        if (instance == null) {
            instance = new ParkingLot(totalSpots, pricingStrategy);
        }
        return instance;
    }

    /**
     * Attempts to park the given car in the first available spot.
     * <p>
     * Before parking, checks whether a car with the same license plate is already present in the lot.
     * If so, the car will not be added again and the method returns {@code false}.
     * If the car is not present and a free spot exists, parks the car, sets its entry time,
     * notifies observers, and returns {@code true}.
     *
     * @param car the car to add
     * @return {@code true} if the car was successfully parked; {@code false} if the lot is full
     *         or the car is already present in the lot
     */
    public boolean addCar(Car car) {
        // Check if car with same license plate is already present
        for (ParkingSpot spot : spots) {
            if (spot.isOccupied() && spot.getParkedCar().getLicensePlate().equals(car.getLicensePlate())) {
                return false; // Car already in the lot
            }
        }
        for (ParkingSpot spot : spots) {
            if (!spot.isOccupied()) {
                spot.parkCar(car);
                car.setEntryTime(System.currentTimeMillis());
                notifyObservers();
                return true;
            }
        }
        return false; // No free spot
    }

    /**
     * Attempts to remove a car by license plate.
     * Sets the exit time, calculates price, adds an entry to history, and notifies observers.
     *
     * @param licensePlate the license plate of the car to remove
     * @return true if the car was found and removed; false otherwise
     */
    public boolean removeCar(String licensePlate) {
        for (ParkingSpot spot : spots) {
            if (spot.isOccupied() && spot.getParkedCar().getLicensePlate().equals(licensePlate)) {
                Car car = spot.getParkedCar();
                car.setExitTime(System.currentTimeMillis());
                double price = pricingStrategy.calculatePrice(car.getEntryTime(), car.getExitTime());
                history.add(new CarHistoryEntry(car.clone(), car.getEntryTime(), car.getExitTime(), price));
                spot.removeCar();
                notifyObservers();
                return true;
            }
        }
        return false; // Car not found
    }

    /**
     * Registers an observer to receive notifications on changes.
     *
     * @param observer the observer to add
     */
    public void addObserver(ParkingLotObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers of a change in the parking lot.
     */
    public void notifyObservers() {
        for (ParkingLotObserver observer : observers) {
            observer.update(this);
        }
    }

    // --- Statistics and History ---

    /**
     * Returns the total number of parking spots in the lot.
     *
     * @return total spots
     */
    public int getTotalSpots() {
        return spots.size();
    }

    /**
     * Returns the number of currently occupied spots.
     *
     * @return occupied spots count
     */
    public int getOccupiedSpots() {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (spot.isOccupied()) count++;
        }
        return count;
    }

    /**
     * Returns the number of currently free spots.
     *
     * @return free spots count
     */
    public int getFreeSpots() {
        return getTotalSpots() - getOccupiedSpots();
    }

    /**
     * Returns the average parking session time in minutes.
     *
     * @return average parking time in minutes, or 0 if no history
     */
    public double getAverageParkingTimeMinutes() {
        if (history.isEmpty()) return 0;
        double total = 0;
        for (CarHistoryEntry entry : history) {
            total += (entry.getExitTime() - entry.getEntryTime());
        }
        return total / history.size() / (1000 * 60.0);
    }

    /**
     * Returns today's total revenue for all cars that exited today.
     *
     * @return revenue in NIS (or other currency)
     */
    public double getTodaysRevenue() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int dayOfYear = today.get(Calendar.DAY_OF_YEAR);
        double sum = 0;
        for (CarHistoryEntry entry : history) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(entry.getExitTime());
            if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.DAY_OF_YEAR) == dayOfYear) {
                sum += entry.getPaid();
            }
        }
        return sum;
    }

    /**
     * Returns the complete list of all parking history entries.
     *
     * @return list of history entries
     */
    public List<CarHistoryEntry> getHistory() {
        return history;
    }

    /**
     * Returns the current list of parking spots.
     *
     * @return list of parking spots
     */
    public List<ParkingSpot> getSpots() {
        return spots;
    }
}