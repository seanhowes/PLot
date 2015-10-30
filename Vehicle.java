package ca.sph;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class Vehicle {

    public LocalTime getWaitTime() {
        return waitTime;
    }

    private LocalTime waitTime = LocalTime.now().plusSeconds(new Random(new Date().getTime()).nextInt(60));
    public String toString() {
        return waitTime.format(DateTimeFormatter.ofPattern(" HH:mm:ss "));
    }


}
