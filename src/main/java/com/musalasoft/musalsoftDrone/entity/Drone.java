package com.musalasoft.musalsoftDrone.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "drones")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Drone extends Base{

    @Transient
    private final Logger logger = LoggerFactory.getLogger(Drone.class);

    public Drone(Long id) {
        this.id = id;
    }

    public Drone() {

    }


    @NotBlank
    @Length(min = 1, max = 100)
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    private String serialNumber;

    @NotNull
    @Max(500)
    @Min(1)
    @Getter
    @Column(nullable = false)
    private Integer weightLimit;

    @NotNull
    @Max(100)
    @Min(0)
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer batteryCapacity = 100;

    public enum Model {
        LIGHT_WEIGHT(125),
        MIDDLE_WEIGHT(250),
        CRUISER_WEIGHT(375),
        HEAVY_WEIGHT(500);

        public final Integer weightLimit;


        Model(Integer weightLimit) {
            this.weightLimit = weightLimit;
        }
    }

    @NotNull
    @Getter
    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Model model;

    public enum State {
        IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING
    }

    @NotNull
    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state = State.IDLE;

    @Getter
    @Setter
    @OneToMany(mappedBy = "drone", orphanRemoval = true)
    @JsonManagedReference
    private Collection<Medication> medications = new ArrayList<>();

    public void setModel(Model model) {
        this.model = model;
        weightLimit = model.weightLimit;
    }

    public void setState(State state) {

        String log;
        if(state.equals(State.IDLE)) {
             log =  "Drone is Still Charging in IDLE State";
         } else {
           log = String.format("Drone Transitioned from %s State to %s State", this.state, state);
        }

        this.state = state;

        logger.info("[{}] [{}%] -- {}", serialNumber, batteryCapacity, log);
    }

}
