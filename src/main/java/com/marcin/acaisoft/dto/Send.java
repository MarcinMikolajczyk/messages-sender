package com.marcin.acaisoft.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Send {
    @NotNull(message = "Magic number cannot be null")
    @JsonProperty("magic_number")
    private Integer magicNumber;
}
