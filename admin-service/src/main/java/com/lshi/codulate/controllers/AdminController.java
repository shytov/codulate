package com.lshi.codulate.controllers;

import com.lshi.codulate.dto.PointDto;
import com.lshi.codulate.model.GeoZone;
import com.lshi.codulate.service.GeoZoneService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Validated
@RestController
@AllArgsConstructor
public class AdminController {
    public static Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final GeoZoneService geoZoneService;
    private final MessageSource messageSource;

    @PostMapping(value = "/add_zone")
    public GeoZone addZone(@RequestBody PointDto[] path, @RequestParam(required = true) String name) {
        LOG.info("Get zone:  {} | {}", path, name);

        if(name.isEmpty() || name.length() < 3) {
            throw new IllegalArgumentException(messageSource.getMessage("javax.validation.constraints.Size.message", new Object[]{}, Locale.ROOT));
        }

        return geoZoneService.saveFromDto(name, path);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
