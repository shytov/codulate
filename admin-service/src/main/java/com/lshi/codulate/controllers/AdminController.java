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

    /**
     * Add new Zone with name and path
     * @param path
     * @param name
     * @return
     */
    @PostMapping(value = "/add_zone")
    public GeoZone addZone(@RequestBody PointDto[] path, @RequestParam(required = true) String name) {
        LOG.info("Get zone:  {} | {}", path, name);

        if(name.isEmpty() || name.length() < 3) {
            throw new IllegalArgumentException(messageSource.getMessage("javax.validation.constraints.Size.message", new Object[]{}, Locale.ROOT));
        }

        return geoZoneService.saveFromDto(name, path);
    }

    /**
     *    Simple load test generator. Gen 100 1x1 squares shifted by 1
     *    ________
     *    |       |
     *    |       |
     *    |_______|         ___________           ___________
     *    0       1        2          3          4          5
     *
     * @return
     */
    @PostMapping(value = "/load_test_gen")
    public String genBigData() {
        int j = 0;
        for(int i = 0; i < 100; i++) {
            String name = "Zone " + i;
            PointDto[] path = new PointDto[] {
                    new PointDto(j, 0, 0),
                    new PointDto(j + 1, 0, 0),
                    new PointDto(j + 1, 1, 0),
                    new PointDto(j, 1, 0),
                    new PointDto(j, 0, 0)};
            j = j + 2;
            geoZoneService.saveFromDto(name, path);
        }
        return "0";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
