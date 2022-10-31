package com.musalasoft.musalsoftDrone.repository;

import com.musalasoft.musalsoftDrone.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone,Long> {

    Drone getBySerialNumber(String serialNumber);

    List<Drone> findAllByState(Drone.State state);

}
