package ru.kubsu.geocoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kubsu.geocoder.client.NominatimClient;
import ru.kubsu.geocoder.model.Address;
import ru.kubsu.geocoder.repository.AddressRepository;

import java.util.Optional;

/**
 *
 */
@Service
public class AddressService {

    private static final String QUERY_FOR_REVERSE = null;
    private final NominatimClient nominatimClient;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(final NominatimClient nominatimClient,
                          final AddressRepository addressRepository) {
        this.nominatimClient = nominatimClient;
        this.addressRepository = addressRepository;
    }

    public Optional<Address> search(final String query) {
        return addressRepository.findByQuery(query)
                .or(() -> nominatimClient.search(query)
                        .map(place -> addressRepository.save(Address.of(place, query)))
                );
    }

    public Optional<Address> reverse(final String latitude, final String longitude) {
        try {
            final Double lat = Double.parseDouble(latitude);
            final double lon = Double.parseDouble(longitude);
           return addressRepository
                   .findByLatitudeAndLongitude(lat, lon)
                   .or(() -> nominatimClient.reverse(latitude, longitude)
                           //может быть найдено место, которое уже есть в таблице
                           .map(place -> addressRepository
                                   .findByLatitudeAndLongitude(place.latitude(), place.longitude())
                                   .orElseGet(() -> addressRepository.save(Address.of(place, QUERY_FOR_REVERSE)))
                           )
                   );
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
