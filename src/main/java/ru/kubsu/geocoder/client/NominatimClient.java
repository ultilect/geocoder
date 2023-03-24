/**
 * Copyright 2023 Bogdan Lesin
 */

package ru.kubsu.geocoder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kubsu.geocoder.dto.NominatimPlace;

import java.util.List;
import java.util.Optional;

/**
 * @author Bogdan Lesin
 */
@FeignClient(value = "nominatim", url = "https://nominatim.openstreetmap.org/")
public interface NominatimClient {
    String JSON_FORMAT = "json";

    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    List<NominatimPlace> search(@RequestParam("q") String query,
                                @RequestParam(value = "format", defaultValue = "json") String format);
    /**
     * Поиск объекта на карте по адресной строке в свободном формате.
     * В случае нескольких подходящих объектов, будет возвращен самый релевантный.
     *
     * @param query Строка поиска.
     * @return Объект адреса.
     */

    default Optional<NominatimPlace> search(final String query) {
        try {
            return Optional.of(search(query, JSON_FORMAT).get(0));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reverse", produces = MediaType.APPLICATION_JSON_VALUE)
    NominatimPlace reverse(@RequestParam("lat") String latitude,
                           @RequestParam("lon") String longitude,
                           @RequestParam(value = "format", defaultValue = "json") String format);

    default Optional<NominatimPlace> reverse(final String latitude, final String longitude) {
        try {
            return Optional.of(reverse(latitude, longitude, JSON_FORMAT));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
