package com.musalasoft.musalsoftDrone.payload.dto.drone;

import com.musalasoft.musalsoftDrone.entity.Drone;
import lombok.Getter;

@Getter
public class DroneDto {

    private final Long id;

    private final String serialNumber;

    private final Integer weightLimit;

    private final Integer batteryCapacity;

    private final Drone.Model model;

    private final Drone.State state;


    public DroneDto(Long id, String serialNumber, Integer weightLimit, Integer batteryCapacity, Drone.Model model, Drone.State state) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.model = model;
        this.state = state;
    }

    public DroneDto(Drone drone) {
        id = drone.getId();
        serialNumber= drone.getSerialNumber();
        weightLimit = drone.getWeightLimit();
        batteryCapacity = drone.getBatteryCapacity();
        model = drone.getModel();
        state = drone.getState();
    }
}
