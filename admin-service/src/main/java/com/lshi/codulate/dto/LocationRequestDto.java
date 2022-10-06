package com.lshi.codulate.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {
    private String zoneName;
    private CoordinateDto point;
    private PathDto path;
}
