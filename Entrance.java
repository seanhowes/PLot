package ca.sph;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Entrance implements Runnable {

    private final ParkingLot parkingLot;
    private final BlockingQueue<Vehicle> entranceQueue = new LinkedBlockingQueue<>(); //todo you need to work out the data type now

    public Entrance(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }


    @Override
    public void run() {
        while (true) { /// nope nope nope
            if (entranceQueue.peek() != null) {
                if (parkingLot.park(entranceQueue.peek())) {
                    entranceQueue.poll();
                }

            }
        }
    }

    public int size() {
        return entranceQueue.size();
    }

    public void add(Vehicle vehicle) {
        entranceQueue.add(vehicle);
    }
}
