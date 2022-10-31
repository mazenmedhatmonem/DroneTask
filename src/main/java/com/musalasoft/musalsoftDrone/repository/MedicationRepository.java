package com.musalasoft.musalsoftDrone.repository;

import com.musalasoft.musalsoftDrone.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication,Integer> {

}
