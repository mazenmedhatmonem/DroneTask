package com.musalasoft.musalsoftDrone.payload.dto.medication;

import com.musalasoft.musalsoftDrone.entity.Medication;
import lombok.Getter;

@Getter
public class MedicationDto {

    private final Long id;

    private final String name;

    private final Integer weight;

    private final String code;

    private final String imageUrl;

    private final Boolean isDelivered;

    public MedicationDto(Long id, String name, Integer weight, String code, String imageUrl, Boolean isDelivered) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.imageUrl = imageUrl;
        this.isDelivered = isDelivered;
    }

    public MedicationDto(Medication medication) {
        id = medication.getId();
        name = medication.getName();
        weight = medication.getWeight();
        code = medication.getCode();
        imageUrl = medication.getImageUrl();
        isDelivered = medication.getIsDelivered();
    }
}
