package com.musalasoft.musalsoftDrone.seeder;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.repository.DroneRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DroneModelSeeder implements Seeder {
    private final DroneRepository droneRepository;

    public DroneModelSeeder(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public void seed() {
        if (droneRepository.count() == 0) {
            Drone.Model[] models = Drone.Model.values();
            List<Drone> drones = new ArrayList<>(models.length);
            for (Drone.Model model : models) {
                Drone drone = new Drone();

                drone.setModel(model);
                drone.setSerialNumber(UUID.randomUUID().toString());

                drones.add(drone);
            }

            droneRepository.saveAll(drones);

        }
    }
}
