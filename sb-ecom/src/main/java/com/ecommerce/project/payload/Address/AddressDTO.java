package com.ecommerce.project.payload.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long addressId;

    @NotBlank(message = "Street cannot be blank")
    private String street;

    @NotBlank(message = "Building name cannot be blank")
    private String building;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotBlank(message = "Pincode cannot be blank")
    @Size(min = 5, message = "Pincode must be at least 5 characters")
    private String pincode;
}
