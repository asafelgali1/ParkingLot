/**
 * AllUnitTests.java
 * <p>
 * This file contains comprehensive unit tests for the main logic classes of the Smart Parking Lot project.
 * Each inner class tests a specific component, ensuring correctness of all main methods and behaviors.
 * All tests are implemented using JUnit 5.
 * <p>
 * Classes tested:
 * - Car
 * - CarHistoryEntry
 * - HourlyPricingStrategy & PricingStrategy
 * - ParkingSpot
 * - ParkingLot
 * - RegularCarFactory & CarFactory
 * - ParkingLotObserver
 * <p>
 * To run these tests, ensure you have JUnit 5 in your classpath.
 */

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AllUnitTests {

    // ------------------- Car Tests -------------------
    @Nested
    class CarTest {

        @Test
        void testCarConstructorAndGetters() {
            Car car = new Car("123-45-678");
            assertEquals("123-45-678", car.getLicensePlate());
            assertEquals(-1, car.getEntryTime());
            assertEquals(-1, car.getExitTime());
        }

        @Test
        void testSetEntryAndExitTime() {
            Car car = new Car("123-45-678");
            car.setEntryTime(1000L);
            car.setExitTime(5000L);
            assertEquals(1000L, car.getEntryTime());
            assertEquals(5000L, car.getExitTime());
        }

        @Test
        void testClone() {
            Car car = new Car("123-45-678");
            car.setEntryTime(1000L);
            car.setExitTime(5000L);
            Car clone = car.clone();
            assertNotSame(car, clone);
            assertEquals(car.getLicensePlate(), clone.getLicensePlate());
            assertEquals(car.getEntryTime(), clone.getEntryTime());
            assertEquals(car.getExitTime(), clone.getExitTime());
        }

        @Test
        void testToString() {
            Car car = new Car("123-45-678");
            String s = car.toString();
            assertTrue(s.contains("123-45-678"));
        }
    }

    // ------------------- CarHistoryEntry Tests -------------------
    @Nested
    class CarHistoryEntryTest {
        @Test
        void testCarHistoryEntryConstructorAndGetters() {
            Car car = new Car("555-33-222");
            CarHistoryEntry entry = new CarHistoryEntry(car, 1000L, 5000L, 20.0);
            assertEquals(car, entry.getCar());
            assertEquals(1000L, entry.getEntryTime());
            assertEquals(5000L, entry.getExitTime());
            assertEquals(20.0, entry.getPaid(), 0.001);
        }
    }

    // ------------------- PricingStrategy/HourlyPricingStrategy Tests -------------------
    @Nested
    class HourlyPricingStrategyTest {
        @Test
        void testCalculatePriceOneHour() {
            PricingStrategy strategy = new HourlyPricingStrategy(10.0);
            long entry = 0L;
            long exit = 60 * 60 * 1000L; // 1 hour later
            assertEquals(10.0, strategy.calculatePrice(entry, exit), 0.001);
        }

        @Test
        void testCalculatePricePartialHourRoundsUp() {
            PricingStrategy strategy = new HourlyPricingStrategy(10.0);
            long entry = 0L;
            long exit = (long) (1.5 * 60 * 60 * 1000L); // 1.5 hours
            assertEquals(20.0, strategy.calculatePrice(entry, exit), 0.001);
        }

        @Test
        void testCalculatePriceZeroDuration() {
            PricingStrategy strategy = new HourlyPricingStrategy(10.0);
            assertEquals(0.0, strategy.calculatePrice(1000L, 1000L), 0.001);
        }

        @Test
        void testImplementsPricingStrategy() {
            PricingStrategy s = new HourlyPricingStrategy(15.0);
            assertInstanceOf(PricingStrategy.class, s);
            assertEquals(15.0, s.calculatePrice(0, 60*60*1000), 0.001);
        }
    }

    // ------------------- ParkingSpot Tests -------------------
    @Nested
    class ParkingSpotTest {
        @Test
        void testParkingSpotInitiallyEmpty() {
            ParkingSpot spot = new ParkingSpot();
            assertFalse(spot.isOccupied());
            assertNull(spot.getParkedCar());
        }

        @Test
        void testParkCarAndRemoveCar() {
            ParkingSpot spot = new ParkingSpot();
            Car car = new Car("123-45-678");
            spot.parkCar(car);
            assertTrue(spot.isOccupied());
            assertEquals(car, spot.getParkedCar());

            spot.removeCar();
            assertFalse(spot.isOccupied());
            assertNull(spot.getParkedCar());
        }
    }

    // ------------------- ParkingLot Tests -------------------
    @Nested
    class ParkingLotTest {
        private ParkingLot parkingLot;

        @BeforeEach
        void setUp() {
            // Reset singleton for each test (reflection, for testing only!)
            try {
                java.lang.reflect.Field instanceField = ParkingLot.class.getDeclaredField("instance");
                instanceField.setAccessible(true);
                instanceField.set(null, null);
            } catch (Exception ignored) {}

            parkingLot = ParkingLot.createLot(2, new HourlyPricingStrategy(10.0));
        }

        @Test
        void testAddCarAndRemoveCar() {
            Car car = new Car("111-11-111");
            assertTrue(parkingLot.addCar(car));
            assertEquals(1, parkingLot.getOccupiedSpots());
            assertFalse(parkingLot.addCar(car)); // Should not add same car again

            assertTrue(parkingLot.removeCar(car.getLicensePlate()));
            assertEquals(0, parkingLot.getOccupiedSpots());
        }

        @Test
        void testAddCarWhenLotFull() {
            assertTrue(parkingLot.addCar(new Car("1")));
            assertTrue(parkingLot.addCar(new Car("2")));
            assertFalse(parkingLot.addCar(new Car("3"))); // No more spots
        }

        @Test
        void testRemoveNonExistingCar() {
            assertFalse(parkingLot.removeCar("not-exist"));
        }

        @Test
        void testObserversAreNotified() {
            AtomicBoolean notified = new AtomicBoolean(false);
            parkingLot.addObserver(_ -> notified.set(true));
            parkingLot.addCar(new Car("555-33-222"));
            assertTrue(notified.get());
        }

        @Test
        void testStatistics() {
            parkingLot.addCar(new Car("100"));
            parkingLot.addCar(new Car("200"));
            assertEquals(2, parkingLot.getOccupiedSpots());
            assertEquals(0, parkingLot.getFreeSpots());
            assertEquals(2, parkingLot.getTotalSpots());
        }

        @Test
        void testHistoryAndRevenue() throws InterruptedException {
            Car car = new Car("101");
            parkingLot.addCar(car);
            Thread.sleep(10); // Short wait for time difference
            parkingLot.removeCar(car.getLicensePlate());

            List<CarHistoryEntry> hist = parkingLot.getHistory();
            assertEquals(1, hist.size());
            assertEquals("101", hist.getFirst().getCar().getLicensePlate());
            assertTrue(hist.getFirst().getPaid() > 0);

            // Today's revenue should be positive
            assertTrue(parkingLot.getTodaysRevenue() > 0);
        }
    }

    // ------------------- RegularCarFactory & CarFactory Tests -------------------
    @Nested
    class RegularCarFactoryTest {
        @Test
        void testCreateCar() {
            CarFactory factory = new RegularCarFactory();
            Car car = factory.createCar("123-45-678");
            assertNotNull(car);
            assertEquals("123-45-678", car.getLicensePlate());
        }
    }

    // ------------------- ParkingLotObserver Tests -------------------
    @Nested
    class ParkingLotObserverTest {
        @Test
        void testObserverUpdateCalled() {
            // Reset singleton
            try {
                java.lang.reflect.Field instanceField = ParkingLot.class.getDeclaredField("instance");
                instanceField.setAccessible(true);
                instanceField.set(null, null);
            } catch (Exception ignored) {}
            ParkingLot lot = ParkingLot.createLot(1, new HourlyPricingStrategy(10.0));

            final boolean[] called = {false};
            ParkingLotObserver observer = new ParkingLotObserver() {
                @Override
                public void update(ParkingLot l) {
                    called[0] = true;
                }
            };
            lot.addObserver(observer);
            lot.addCar(new Car("123"));
            assertTrue(called[0]);
        }
    }
}