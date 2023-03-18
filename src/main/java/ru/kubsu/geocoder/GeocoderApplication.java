package ru.kubsu.geocoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@SuppressWarnings({"PMD.UseUtilityClass", "HideUtilityClassConstructor", "MissingJavadocType"})
public class GeocoderApplication {

    public static void main(final String[] args) {
        SpringApplication.run(GeocoderApplication.class, args);
    }

}
