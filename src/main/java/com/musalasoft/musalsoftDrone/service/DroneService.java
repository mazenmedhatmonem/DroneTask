package com.musalasoft.musalsoftDrone.service;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.exception.ServiceException;
import com.musalasoft.musalsoftDrone.payload.dto.drone.DroneDto;
import com.musalasoft.musalsoftDrone.payload.request.drone.CreateDroneRequest;
import com.musalasoft.musalsoftDrone.repository.DroneRepository;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Value("${musalaSoft.service.maxDrones}")
    private Integer maxDrones;

    public DroneService(DroneRepository droneRepository, EntityManager entityManager) {
        this.droneRepository = droneRepository;
        this.entityManager = entityManager;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public synchronized Drone createDrone(CreateDroneRequest createDroneRequest) {


        long count = droneRepository.count();

        if (count >= maxDrones) throw new ServiceException("Maximum Drone Limit Reached");

        Drone drone = new Drone();

        drone.setSerialNumber(createDroneRequest.getSerialNumber());
        drone.setModel(createDroneRequest.getModel());

        drone.setState(Drone.State.LOADING);

        return droneRepository.save(drone);

    }



    public List<DroneDto> getAvailableDrones() {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<DroneDto> query = builder.createQuery(DroneDto.class);
        Root<Drone> from = query.from(Drone.class);

        query.select(
                builder.construct(
                        DroneDto.class,
                        from.get("id"),
                        from.get("serialNumber"),
                        from.get("weightLimit"),
                        from.get("batteryCapacity"),
                        from.get("model"),
                        from.get("state")
                )
        );
        query.where(builder.equal(from.get("state"), Drone.State.LOADING));

        return entityManager.createQuery(query).getResultList();
    }
    public Drone getDroneBySerialNumber(String serialNumber) {
        return droneRepository.getBySerialNumber(serialNumber);
    }
}
