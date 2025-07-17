/**
 * The {@code ParkingLotObserver} interface should be implemented by any class
 * that wishes to be notified when the state of a {@link ParkingLot} changes.
 * <p>
 * This interface is part of the Observer design pattern. Observers are registered
 * with the {@link ParkingLot} and are notified via the {@link #update(ParkingLot)} method
 * whenever a relevant change occurs (such as a car entering or exiting the lot).
 * <p>
 * Example usage:
 * <pre>
 *     class MyObserver implements ParkingLotObserver {
 *         public void update(ParkingLot lot) {
 *             // React to parking lot changes
 *         }
 *     }
 * </pre>
 */
public interface ParkingLotObserver {
    /**
     * Called when the parking lot's state changes.
     *
     * @param lot the updated ParkingLot instance
     */
    void update(ParkingLot lot);
}