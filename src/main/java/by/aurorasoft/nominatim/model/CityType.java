package by.aurorasoft.nominatim.model;

import by.aurorasoft.nominatim.model.OverpassTurboSearchCityResponse.Tags;

import java.util.Objects;

public enum CityType {
    CAPITAL {
        @Override
        public boolean match(final Tags tags) {
            return Objects.equals(SUITABLE_CAPITAL_VALUE, tags.getCapital());
        }
    },

    CITY {
        @Override
        public boolean match(final Tags tags) {
            return tags.getCapital() == null && Objects.equals(CITY_PLACE_VALUE, tags.getPlace());
        }
    },

    TOWN {
        @Override
        public boolean match(final Tags tags) {
            return Objects.equals(TOWN_PLACE_VALUE, tags.getPlace());
        }
    };

    private static final String SUITABLE_CAPITAL_VALUE = "yes";
    private static final String CITY_PLACE_VALUE = "city";
    private static final String TOWN_PLACE_VALUE = "town";

    public abstract boolean match(final Tags tags);
}
