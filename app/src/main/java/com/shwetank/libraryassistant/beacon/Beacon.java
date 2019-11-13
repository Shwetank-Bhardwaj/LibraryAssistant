package com.shwetank.libraryassistant.beacon;

import java.util.UUID;

public class Beacon {
    private UUID uuid;
    private int major;
    private int minor;
    private Proximity proximityRange;

    public Beacon(UUID uuid, int major, int minor) {

        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public void setProximityRange(Proximity proximityRange){
        this.proximityRange = proximityRange;
    }

    public Proximity getProximityRange(){
        return proximityRange;
    }

}