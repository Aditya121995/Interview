package com.problems.RideBookingSystem;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        RideService rideService = RideService.getInstance(new DistanceTimeFareStrategy(), new NearestDriverMatchingStrategy(),
                new AverageTimeCalculation());

        Passenger p1 = new Passenger("p1", "person1", "7388239923", new Location(1,2));
        Passenger p2 = new Passenger("p2", "person2", "73882549923", new Location(3,5));

        Driver driver1 = new Driver("d1", "driver1", "342293492",
                new Vehicle("KA-120248", RideType.REGULAR, "swift"),
                new Location(0,0));

        Driver driver2 = new Driver("d2", "driver2", "342293494",
                new Vehicle("KA-120568", RideType.REGULAR, "wagonR"),
                new Location(1,1));

        Driver driver3 = new Driver("d3", "driver3", "342296794",
                new Vehicle("KA-150568", RideType.REGULAR, "wagonR"),
                new Location(10,10));

        Driver driver4 = new Driver("d4", "driver4", "342493494",
                new Vehicle("KA-157868", RideType.PREMIUM, "innova"),
                new Location(5,5));

        Driver driver5 = new Driver("d5", "driver5", "342425494",
                new Vehicle("KA-1576778", RideType.PREMIUM, "innova"),
                new Location(6,10));

        rideService.registerPassenger(p1);
        rideService.registerPassenger(p2);

        rideService.registerDriver(driver1);
        rideService.registerDriver(driver2);
        rideService.registerDriver(driver3);
        rideService.registerDriver(driver4);
        rideService.registerDriver(driver5);

        Ride ride = rideService.requestRide(p1, new Location(2, 1), new Location(10, 5.5), RideType.REGULAR, 0.5);


        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        for (int i = 1; i <= 2; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rideService.acceptRide("d" + threadNum, ride.getRideId());
            });
        }

        executor.shutdown();




//        if (ride != null) {
//            rideService.acceptRide("d1", ride.getRideId());
//            rideService.startRide("d1", ride.getRideId());
//            rideService.completeRide("d1", ride.getRideId());
//        }



    }
}
