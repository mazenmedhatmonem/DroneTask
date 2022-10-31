package com.musalasoft.musalsoftDrone.payload.request.medication;

import com.musalasoft.musalsoftDrone.validator.annotations.Regex;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class CreateMedicationRequest {

    @NotBlank
    @Length(min = 1, max = 400)
    @Regex(required = true, regex = "^[\\w\\-]+$")
    @Getter
    @Setter
    private String name;


    @NotNull(message = "value is required")
    @Positive()
    @Getter
    @Setter
    private Integer weight;

    @NotBlank
    @Length(min = 1, max = 400)
    @Regex(required = true, regex = "^[\\dA-Z_]+$")
    @Getter
    @Setter
    private String code;

    @NotBlank
    @Length(min = 1, max = 500)
    @URL
    @Getter
    @Setter
    private String imageUrl;
}
