package com.kushagra.nexcart.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String street;


    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Boolean isDefault;
}