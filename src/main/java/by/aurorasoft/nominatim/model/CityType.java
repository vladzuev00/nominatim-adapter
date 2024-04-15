package by.aurorasoft.nominatim.model;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public enum CityType {
    CAPITAL("yes"), REGIONAL("4"), NOT_DEFINED("not defined");

    private final String jsonValue;

    public static CityType findByJsonValue(String value) {
        return stream(values())
                .filter(type -> Objects.equals(value, type.jsonValue))
                .findAny()
                .orElse(NOT_DEFINED);
    }
}
