package ru.kubsu.geocoder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kubsu.geocoder.dto.NominatimPlace;

import java.util.List;

@FeignClient(value = "nominatim", url = "https://nominatim.openstreetmap.org/")
public interface NominatimClient {
    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = "application/json")
    List<NominatimPlace> search(@RequestParam(value = "q") String query,
                                @RequestParam(value = "format", defaultValue = "json") String format);
}
