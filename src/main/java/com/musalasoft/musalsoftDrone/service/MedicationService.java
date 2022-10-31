package com.musalasoft.musalsoftDrone.service;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.entity.Medication;
import com.musalasoft.musalsoftDrone.exception.ServiceException;
import com.musalasoft.musalsoftDrone.payload.dto.medication.MedicationDto;
import com.musalasoft.musalsoftDrone.payload.request.medication.CreateMedicationRequest;
import com.musalasoft.musalsoftDrone.repository.MedicationRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final MedicationRepository medicationRepository;

    public MedicationService(EntityManager entityManager, MedicationRepository medicationRepository) {
        this.entityManager = entityManager;
        this.medicationRepository = medicationRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void addMedication(List<CreateMedicationRequest> createMedicationRequests, String serialNumber) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Drone> query = builder.createQuery(Drone.class);
        Root<Drone> from = query.from(Drone.class);
        query.where(builder.equal(from.get("serialNumber"), serialNumber));
        Drone drone = entityManager.createQuery(query).setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();

        if (drone.getState() != Drone.State.LOADING) {
            throw new ServiceException("Drone is not in loading state");
        }

        if (drone.getBatteryCapacity() < 25) {
            throw new ServiceException("Drone has low battery level");
        }

        List<Medication> medications = new ArrayList<>(createMedicationRequests.size());
        Integer totalWeight = 0;
        for (CreateMedicationRequest createMedicationRequest : createMedicationRequests) {
            Integer weight = createMedicationRequest.getWeight();
            totalWeight += weight;

            Medication medication = new Medication();
            medication.setCode(createMedicationRequest.getCode());
            medication.setImageUrl(createMedicationRequest.getImageUrl());
            medication.setWeight(weight);
            medication.setName(createMedicationRequest.getName());

            medication.setDrone(drone);

            medications.add(medication);
        }


        if (drone.getWeightLimit() < totalWeight) {
            throw new ServiceException("Medication weight is above drone weight capacity");
        }

        drone.setState(Drone.State.LOADED);
        entityManager.persist(drone);
        medicationRepository.saveAll(medications);

    }

    public List<MedicationDto> getMedications(String serialNumber) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<MedicationDto> query = builder.createQuery(MedicationDto.class);
        Root<Medication> from = query.from(Medication.class);

        query.select(
                builder.construct(
                        MedicationDto.class,
                        from.get("id"),
                        from.get("name"),
                        from.get("weight"),
                        from.get("code"),
                        from.get("imageUrl"),
                        from.get("isDelivered")
                )
        );
        query.where(builder.and(
                builder.equal(from.get("drone").get("serialNumber"), serialNumber),
                builder.isFalse(from.get("isDelivered"))
        ));

        return entityManager.createQuery(query).getResultList();
    }


}
