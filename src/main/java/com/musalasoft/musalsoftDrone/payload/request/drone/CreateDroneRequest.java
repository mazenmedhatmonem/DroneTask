package com.musalasoft.musalsoftDrone.payload.request.drone;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.validator.annotations.Unique;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateDroneRequest {

    @NotBlank
    @Length(min = 1, max = 100)
    @NotNull(message = "value is required")
    @Unique(field = "serialNumber", required = true, entity = Drone.class)
    @Getter
    @Setter
    private String serialNumber;

    @Getter
    @Setter
    @NotNull(message = "value is required")
    private Drone.Model model;

}
