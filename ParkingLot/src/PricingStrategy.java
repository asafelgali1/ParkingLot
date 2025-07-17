/**
 * The {@code PricingStrategy} interface defines a strategy for calculating the price
 * of a parking session based on entry and exit times.
 * <p>
 * Implementations of this interface can provide different pricing algorithms, such as
 * hourly rates, flat rates, or special discounts.
 * <p>
 * Example usage:
 * <pre>
 *     PricingStrategy strategy = new HourlyPricingStrategy(10.0);
 *     double price = strategy.calculatePrice(entryTime, exitTime);
 * </pre>
 */
public interface PricingStrategy {
    /**
     * Calculates the price for a parking session given entry and exit times.
     *
     * @param entryTime the time the car entered (in milliseconds since epoch)
     * @param exitTime  the time the car exited (in milliseconds since epoch)
     * @return the calculated price for the session
     */
    double calculatePrice(long entryTime, long exitTime);
}