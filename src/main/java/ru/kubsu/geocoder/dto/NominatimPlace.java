package ru.kubsu.geocoder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Адрес.
 *
 * @param latitude широта
 * @param longitude долгота
 * @param displayName имя
 * @param type тип
 */
public record NominatimPlace(
        @JsonProperty("lat") Double latitude,
        @JsonProperty("lon") Double longitude,
        @JsonProperty("display_name") String displayName,
        @JsonProperty("type") String type) {
    public NominatimPlace() {
        this(45.044_427_1, 39.028_404_322_949_385, "Стадион 'Краснодар'", "");
    }
}
