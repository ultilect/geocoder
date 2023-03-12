package ru.kubsu.geocoder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class NominatimPlace {
    @JsonProperty("lat")
    Double latitude;
    @JsonProperty("lon")
    Double longitude;
    @JsonProperty("display_name")
    String displayName;
    @JsonProperty("type")
    String type;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NominatimPlace that = (NominatimPlace) o;
        return Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(displayName, that.displayName) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude, displayName, type);
    }

    @Override
    public String toString() {
        return "NominatimPlace{" +
                "lat='" + latitude + '\'' +
                ", lon='" + longitude + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
