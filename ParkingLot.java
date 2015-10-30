package ca.sph;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ParkingLot {

    private static ParkingLot INSTANCE;
    private int capacity;

    int totalAdded = 0;
    int totalLeaving = 0;

    private List<Vehicle> spaces = new ArrayList<>(capacity);
    private int exits; //todo move to exit object ?

    public List<Entrance> getEntranceList() {
        return entranceList;
    }
    private List<Entrance> entranceList = new ArrayList<>();

    private ParkingLot(int entrances, int exits, int capacity) {
        for (int i = 0; i < entrances; i++) {
            final Entrance entrance = new Entrance(this);
            entranceList.add(entrance);
            new Thread(entrance).start(); //todo fix this better

        }
        this.exits = exits;
        this.capacity = capacity;

        System.out.println("Settings Entrances " + entranceList.size() + "  Exits " + this.exits + " Capacity " + this.capacity);

    }


    public static ParkingLot getInstance(int entrances, int exits, int capacity) {
        if (INSTANCE == null) {
            INSTANCE = new ParkingLot(entrances, exits, capacity);
        }
        return INSTANCE;
    }


    public synchronized void tick() { //todo this has to run in its own thread yes or no ???!
        processExits();
        System.out.println(/*spaces + " " + */spaces.size() + "/" + (capacity + spaces.size()) + " " +
                (capacity == 0? "FULL": "SPACE") + " Ent:" + totalAdded + " Exi" + totalLeaving);
    }


    private void processExits() { //todo does this need to be its own thread ?
        final Iterator<Vehicle> iterator = spaces.iterator();
        int canExit = exits;
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();

            if (vehicle.getWaitTime().isBefore(LocalTime.now())) {
                if (canExit > 0) { //todo this sucks
                    iterator.remove();
                    canExit--;
                    capacity++;
                    totalLeaving++;
                }
            }

        }


        for (Entrance q : entranceList) {
            System.out.print(" " + q.size() + " ");
        }
        System.out.print("||");
        System.out.print(" " + (exits - canExit) + " Leaving " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss ")));

    }

    public synchronized boolean park(Vehicle vehicle) {
        if(getCapacity() > 0) {
            spaces.add(vehicle);
            capacity--;
            totalAdded++;
            return true;
        }
        return false; //todo really is this needed must be doing it wrong
    }


    //todo should entrances run own threads ??
    public static void main(String args[]) throws InterruptedException {
        final ParkingLot parkingLot = ParkingLot.getInstance(5, 1, 100); //when set to 100 it was never reached

       int x=0;
        //todo consider other exit conditions -- this is crap code drunk coding fun
        while (true) { //todo move to traffic generator ? while true no no no
            final Random random = new Random(new Date().getTime());
            if (random.nextBoolean()) {

                final int countToAdd = countToAdd();
                x+= countToAdd;
                for(int i = 0; i < countToAdd; i++ ) {
                    parkingLot.getEntranceList().get(new Random(new Date().getTime()).nextInt(parkingLot.getEntranceList().size())).add(new Vehicle());
                    //todo nope just nope nope nope nope
                }
                System.out.println("total added " + x);
            }

            parkingLot.tick();

            Thread.sleep(1000);


        }
    }

    private static int countToAdd() {
        return new Random(new Date().getTime()).nextInt(20);
    }

    private  int getCapacity() {
        return capacity;
    }
}
