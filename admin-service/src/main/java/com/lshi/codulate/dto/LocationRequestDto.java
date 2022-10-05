package com.lshi.codulate.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {
    private CoordinateDto point;
    private PathDto path;
}
