/**
 * The {@code RegularCarFactory} class is a concrete implementation of the {@link CarFactory}
 * abstract class. It creates standard {@link Car} objects using the specified license plate.
 * <p>
 * This class is part of the Factory Method design pattern, allowing for easy extension to
 * other car types (e.g., ElectricCar, Truck, etc.) by implementing different factories.
 * <p>
 * Example usage:
 * <pre>
 *     CarFactory factory = new RegularCarFactory();
 *     Car car = factory.createCar("123-45-678");
 * </pre>
 */
public class RegularCarFactory extends CarFactory {
    /**
     * Creates a new {@link Car} instance with the specified license plate.
     *
     * @param licensePlate the license plate of the car
     * @return a new {@link Car} object
     */
    @Override
    public Car createCar(String licensePlate) {
        return new Car(licensePlate);
    }
}