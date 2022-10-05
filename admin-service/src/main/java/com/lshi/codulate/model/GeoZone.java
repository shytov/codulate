package com.lshi.codulate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "GeoZone")
@Table(name = "geo_zone")
@Getter
@Setter
@ToString
public class GeoZone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_zone_generator")
    @SequenceGenerator(name = "geo_zone_generator", sequenceName = "geo_zone_seq", allocationSize = 1)
    private Long id;
    private String name;
}