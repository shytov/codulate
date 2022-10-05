package com.lshi.mapapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LocationRequestDto {
    private CoordinateDto point;
    private PathDto path;
}
