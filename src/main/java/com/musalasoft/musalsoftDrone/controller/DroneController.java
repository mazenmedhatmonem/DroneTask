package com.musalasoft.musalsoftDrone.controller;

import com.musalasoft.musalsoftDrone.entity.Drone;
import com.musalasoft.musalsoftDrone.payload.dto.drone.DroneDto;
import com.musalasoft.musalsoftDrone.payload.dto.medication.MedicationDto;
import com.musalasoft.musalsoftDrone.payload.request.drone.CreateDroneRequest;
import com.musalasoft.musalsoftDrone.payload.request.medication.CreateMedicationRequest;
import com.musalasoft.musalsoftDrone.payload.response.Response;
import com.musalasoft.musalsoftDrone.payload.response.SuccessResponse;
import com.musalasoft.musalsoftDrone.service.DroneService;
import com.musalasoft.musalsoftDrone.service.MedicationService;
import com.musalasoft.musalsoftDrone.validator.annotations.Exists;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping({"api/v1/drones"})
@Validated
public class DroneController {

    private final DroneService droneService;
    private final MedicationService medicationService;

    public DroneController(DroneService droneService, MedicationService medicationService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> create(@Valid @RequestBody CreateDroneRequest createDroneRequest) {
        Drone drone = droneService.createDrone(createDroneRequest);
        SuccessResponse response = new SuccessResponse();

        response.addDataValue("drone", new DroneDto(drone));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> drones() {

        SuccessResponse response = new SuccessResponse();
        response.addDataValue("drones", droneService.getAvailableDrones());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(path = "/{serialNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> drone(@PathVariable @Valid String serialNumber) {

        SuccessResponse response = new SuccessResponse();
        Drone drone = droneService.getDroneBySerialNumber(serialNumber);
        response.addDataValue("drone", drone != null ? new DroneDto(drone) : null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/{serialNumber}/medications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addMedications(@PathVariable @Valid @Exists(field = "serialNumber", entity = Drone.class) String serialNumber, @Valid @NotNull @NotEmpty @RequestBody List<CreateMedicationRequest> createMedicationRequestList) {
        medicationService.addMedication(createMedicationRequestList, serialNumber);
        SuccessResponse response = new SuccessResponse();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{serialNumber}/medications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addMedications(@PathVariable @Valid String serialNumber) {
        List<MedicationDto> medications = medicationService.getMedications(serialNumber);
        SuccessResponse response = new SuccessResponse();

        response.addDataValue("medications", medications);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
