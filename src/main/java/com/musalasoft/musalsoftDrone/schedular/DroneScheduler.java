package com.musalasoft.musalsoftDrone.schedular;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.entity.Medication;
import com.musalasoft.musalsoftDrone.repository.DroneRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableScheduling
@Transactional
public class DroneScheduler implements Scheduler{

    private final DroneRepository droneRepository;


    @PersistenceContext
    private final EntityManager entityManager;

    public DroneScheduler(DroneRepository droneRepository, EntityManager entityManager) {
        this.droneRepository = droneRepository;
        this.entityManager = entityManager;
    }

    @Scheduled(fixedDelayString = "${musalaSoft.service.simulationDelayMs}")
    public void run() {
        startCharging();
        checkDroneBattery();
        startReturning();
        endDelivery();
        startDelivery();
    }


    private List<Drone> transitionState(Drone.State prev, Drone.State next) {
        List<Drone> drones = droneRepository.findAllByState(prev);
        if (drones.size() == 0) return Collections.emptyList();
        for (Drone drone : drones) {
            drone.setState(next);
        }
        return droneRepository.saveAll(drones);
    }

    private void startDelivery() {
        transitionState(Drone.State.LOADED, Drone.State.DELIVERING);
    }

    void endDelivery() {

        List<Drone> drones = transitionState(Drone.State.DELIVERING, Drone.State.DELIVERED);


        if (drones.size() == 0) return;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaUpdate<Medication> query = builder.createCriteriaUpdate(Medication.class);
        Root<Medication> from = query.from(Medication.class);

        query.set(from.get("isDelivered"), true);

        query.where(from.get("drone").in(drones));

        entityManager.createQuery(query).executeUpdate();


    }

    private void startReturning() {
        transitionState(Drone.State.DELIVERED, Drone.State.RETURNING);
    }

    private void checkDroneBattery() {

        List<Drone> drones = droneRepository.findAllByState(Drone.State.RETURNING);
        if (drones.size() == 0) return;
        for (Drone drone : drones) {
            int batteryCapacity = drone.getBatteryCapacity() - 25;
            drone.setBatteryCapacity(batteryCapacity);

            if (batteryCapacity < 25) {
                drone.setBatteryCapacity(0);
                drone.setState(Drone.State.IDLE);
            } else {
                drone.setState(Drone.State.LOADING);
            }

        }

        droneRepository.saveAll(drones);
    }

    public void startCharging() {

        List<Drone> drones = droneRepository.findAllByState(Drone.State.IDLE);
        if (drones.size() == 0) return;
        for (Drone drone : drones) {
            int batteryCapacity = drone.getBatteryCapacity() + 25;
            drone.setBatteryCapacity(batteryCapacity);

            if (batteryCapacity >= 100) {
                drone.setBatteryCapacity(100);
                drone.setState(Drone.State.LOADING);
            } else {
                drone.setState(Drone.State.IDLE);
            }

        }

        droneRepository.saveAll(drones);
    }

}
