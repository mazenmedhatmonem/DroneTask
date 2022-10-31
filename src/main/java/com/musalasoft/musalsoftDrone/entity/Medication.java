package com.musalasoft.musalsoftDrone.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "medications")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Medication extends Base {

    @NotBlank
    @Length(min = 1, max = 500)
    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @NotNull
    @Max(500)
    @Min(1)
    @Getter
    @Setter
    @Column(nullable = false)
    private Integer weight;

    @NotBlank
    @Length(min = 1, max = 500)
    @Getter
    @Setter
    @Column(nullable = false)
    private String code;

    @NotBlank
    @Length(min = 1, max = 500)
    @Getter
    @Setter
    @Column(nullable = false)
    private String imageUrl;


    @NotNull
    @Getter
    @Setter
    @Column(nullable = false)
    private Boolean isDelivered = false;

    @Setter
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "drone_serial_number",referencedColumnName = "serialNumber")
    @JsonBackReference
    private Drone drone;


}
