package ru.kubsu.geocoder.dto;

/**
 * Ошибка.
 *
 * @param status статус
 * @param error ошибка
 * @param path путь
 */
public record RestApiError(
        Integer status,
        String error,
        String path) {
    public RestApiError() {
        this(0, "", "");
    }
}
