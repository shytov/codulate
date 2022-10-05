package com.lshi.codulate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity(name = "GeoCoordinate")
@Table(name = "geo_coordinate")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GeoCoordinate {
    public GeoCoordinate(GeoZone geoZone, Double x, Double y, Double z) {
        this.geoZone = geoZone;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoCoordinate(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "geo_coordinate_generator")
    @SequenceGenerator(name = "geo_coordinate_generator", sequenceName = "geo_coordinate_seq", allocationSize = 1)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "geo_zone_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private GeoZone geoZone;
    private Double x;
    private Double y;
    private Double z;
}
