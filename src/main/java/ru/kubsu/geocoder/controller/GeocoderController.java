package ru.kubsu.geocoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kubsu.geocoder.dto.RestApiError;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.service.AddressService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("geocoder")
public class GeocoderController {
    private final AddressService addressService;
    @Autowired
    public GeocoderController(final AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Address> search(final @RequestParam("q") String query) {
        return addressService.search(query)
                .map(place -> ResponseEntity.status(HttpStatus.OK).body(place))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @GetMapping(value = "/reverse", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> reverse(final @RequestParam("lat") String latitude,
                                                  final @RequestParam("lon") String longitude) {
        try {
            final Double lat = Double.parseDouble(latitude);
            final Double lon = Double.parseDouble(longitude);
            return addressService.reverse(lat, lon)
                    .map(place -> ResponseEntity.status(HttpStatus.OK).body(place))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception ex) {
            final RestApiError error = new RestApiError(400, "Bad Request", "/geocoder/reverse");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

    }
}
