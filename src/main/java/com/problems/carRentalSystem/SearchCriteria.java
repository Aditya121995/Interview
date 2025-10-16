package com.problems.carRentalSystem;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SearchCriteria {
    private VehicleType vehicleType;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private Double minPricePerHour;
    private Double maxPricePerHour;
}
