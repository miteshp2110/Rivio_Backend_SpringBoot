package com.cts.rivio.dto.request;

import lombok.Data;

@Data
public class LocationRequest {
    private String name;
    private String currencyCode; // Will default to INR if null
    private String timezone;
}
