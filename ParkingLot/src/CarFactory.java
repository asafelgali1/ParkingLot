/**
 * The {@code CarFactory} class is an abstract factory for creating {@link Car} objects.
 * <p>
 * This class implements the Factory Method design pattern, allowing subclasses
 * to define how cars are instantiated. Different types of car factories (for example,
 * {@code RegularCarFactory}, {@code ElectricCarFactory}, etc.) can extend this class
 * and produce specific car types as needed.
 *<p>
 * Usage example:
 * <pre>
 *     CarFactory factory = new RegularCarFactory();
 *     Car car = factory.createCar("123-45-678");
 * </pre>
 */
public abstract class CarFactory {
    /**
     * Creates a new {@link Car} instance with the specified license plate.
     *
     * @param licensePlate the car's license plate
     * @return a new {@link Car} object
     */
    public abstract Car createCar(String licensePlate);
}