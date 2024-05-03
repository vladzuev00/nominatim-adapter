package by.aurorasoft.distanceclassifier;

import by.nhorushko.distancecalculator.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.Value;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;

public final class Runner {
    private static final String FILE_PATH = "./src/test/resources/tracks/2907_track-total_10.53_kobrin_2.9_country_7.63.csv";
    private static final PointParser POINT_PARSER = new PointParser();
    private static final DistanceCalculator DISTANCE_CALCULATOR = new DistanceCalculatorImpl();
    private static final DistanceCalculatorSettings CALCULATOR_SETTINGS = new DistanceCalculatorSettingsImpl(0, 30);

    public static void main(final String[] args) throws Exception {
        final List<Point> points = readPoints();
        final List<PointWithDistance> result = mapToPointsWithDistance(points);
        write(result);
    }

    private static List<Point> readPoints() {
        try (final CSVReader csvReader = new CSVReader(new FileReader(FILE_PATH))) {
            return csvReader.readAll()
                    .stream()
                    .map(POINT_PARSER::parse)
                    .toList();
        } catch (final IOException | CsvException cause) {
            throw new RuntimeException(cause);
        }
    }

    private static void write(final List<PointWithDistance> points)
            throws Exception {
        try (final CSVWriter csvWriter = new CSVWriter(new FileWriter(FILE_PATH))) {
            csvWriter.writeAll(points.stream().map(PointWithDistance::getCsvValues).toList());
        }
    }

    private static List<PointWithDistance> mapToPointsWithDistance(final List<Point> points) {
        final List<PointWithDistance> result = new ArrayList<>();
        result.add(new PointWithDistance(points.get(0), 0, 0));
        for (int i = 1; i < points.size(); i++) {
            final PointWithDistance previous = result.get(result.size() - 1);
            final PointWithDistance pointWithDistance = mapToPointWithDistance(points.get(i), previous);
            result.add(pointWithDistance);
        }
        return result;
    }

    private static PointWithDistance mapToPointWithDistance(final Point point, final PointWithDistance previous) {
        final double relative = DISTANCE_CALCULATOR.calculateDistance(previous, point, CALCULATOR_SETTINGS);
        return new PointWithDistance(point, relative, previous.absolute + relative);
    }

    @Value
    private static class Point implements LatLngAlt {
        Instant datetime;
        float latitude;
        float longitude;
        int altitude;
        int speed;

        @Override
        public boolean isValid() {
            return true;
        }
    }

    @Value
    private static class PointWithDistance implements LatLngAlt {
        Point point;
        double relative;
        double absolute;

        @Override
        public Instant getDatetime() {
            return point.datetime;
        }

        @Override
        public int getAltitude() {
            return point.altitude;
        }

        @Override
        public int getSpeed() {
            return point.speed;
        }

        @Override
        public boolean isValid() {
            return point.isValid();
        }

        @Override
        public float getLatitude() {
            return point.latitude;
        }

        @Override
        public float getLongitude() {
            return point.longitude;
        }

        public String[] getCsvValues() {
            return new String[]{
                    String.valueOf(getLatitude()),
                    String.valueOf(getLongitude()),
                    String.valueOf(getSpeed()),
                    String.valueOf(relative),
                    String.valueOf(absolute),
                    String.valueOf(relative * 1.1),
                    String.valueOf(absolute * 1.1)
            };
        }
    }

    private static final class PointParser {
        private static final int DATE_TIME_INDEX = 1;
        private static final int LATITUDE_INDEX = 2;
        private static final int LONGITUDE_INDEX = 3;
        private static final int ALTITUDE_INDEX = 4;
        private static final int SPEED_INDEX = 5;

        private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern(DATE_TIME_PATTERN);

        public Point parse(final String[] properties) {
            return new Point(
                    parseDateTime(properties),
                    parseLatitude(properties),
                    parseLongitude(properties),
                    parseAltitude(properties),
                    parseSpeed(properties)
            );
        }

        private static Instant parseDateTime(final String[] properties) {
            return LocalDateTime.parse(properties[DATE_TIME_INDEX], DATE_TIME_FORMATTER).toInstant(UTC);
        }

        private static float parseLatitude(final String[] properties) {
            return parseFloat(properties[LATITUDE_INDEX]);
        }

        private static float parseLongitude(final String[] properties) {
            return parseFloat(properties[LONGITUDE_INDEX]);
        }

        private static int parseAltitude(final String[] properties) {
            return parseInt(properties[ALTITUDE_INDEX]);
        }

        private static int parseSpeed(final String[] properties) {
            return parseInt(properties[SPEED_INDEX]);
        }
    }
}
