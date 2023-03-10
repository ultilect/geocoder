package ru.kubsu.geocoder.dto;

import java.util.Objects;

public class RestApiError {
    private Integer status;
    private String error;
    private String path;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestApiError that = (RestApiError) o;
        return Objects.equals(status, that.status) && Objects.equals(error, that.error) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error, path);
    }

    @Override
    public String toString() {
        return "RestApiError{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
