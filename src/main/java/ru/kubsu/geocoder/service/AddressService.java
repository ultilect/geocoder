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

    //TODO: reverse
}
