/**
 * The {@code HourlyPricingStrategy} class implements a pricing strategy where the parking fee
 * is calculated based on the number of hours the car was parked.
 * <p>
 * The price is determined by multiplying the number of hours (rounded up to the next full hour)
 * by the specified hourly rate.
 * <p>
 * Example usage:
 * <pre>
 *     PricingStrategy strategy = new HourlyPricingStrategy(10.0); // 10 NIS per hour
 *     double price = strategy.calculatePrice(entryTime, exitTime);
 * </pre>
 */
public class HourlyPricingStrategy implements PricingStrategy {
    /**
     * The price to be charged per hour of parking.
     */
    private final double pricePerHour;

    /**
     * Constructs a new {@code HourlyPricingStrategy} with the specified hourly rate.
     *
     * @param pricePerHour the price per hour
     */
    public HourlyPricingStrategy(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    /**
     * Calculates the total price based on the entry and exit times.
     * The duration is rounded up to the nearest hour.
     *
     * @param entryTime the time the car entered (in milliseconds since epoch)
     * @param exitTime  the time the car exited (in milliseconds since epoch)
     * @return the total price for the parking duration
     */
    @Override
    public double calculatePrice(long entryTime, long exitTime) {
        long durationMillis = exitTime - entryTime;
        double hours = Math.ceil(durationMillis / (1000.0 * 60 * 60));
        return hours * pricePerHour;
    }
}