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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.time.Instant.now;

public final class Runner {
    private static final Iterator<Instant> INSTANT_ITERATOR = new Iterator<>() {
        private static Instant PREVIOUS = now();

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Instant next() {
            PREVIOUS = PREVIOUS.plus(5, ChronoUnit.MINUTES);
            return PREVIOUS;
        }
    };

    private static final String FILE_PATH = "./src/test/resources/tracks/2907_track-total_10.53_kobrin_2.9_country_7.63.csv";
    private static final PointParser POINT_PARSER = new PointParser();
    private static final DistanceCalculator DISTANCE_CALCULATOR = new DistanceCalculatorImpl();
    private static final DistanceCalculatorSettings CALCULATOR_SETTINGS = new DistanceCalculatorSettingsImpl(0, 30);

    public static void main(final String[] args) throws Exception {
        final List<Point> points = readPoints();
        final List<PointWithDistance> result = calculate(points);
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

    private static List<PointWithDistance> calculate(final List<Point> points) {
        final List<PointWithDistance> result = new ArrayList<>();
        result.add(new PointWithDistance(points.get(0).latitude, points.get(0).longitude, points.get(0).speed, 0, 0, points.get(0).dateTime));
        for (int i = 1; i < points.size(); i++) {
            final PointWithDistance previous = result.get(result.size() - 1);
            final double relative = DISTANCE_CALCULATOR.calculateDistance(points.get(i), previous, CALCULATOR_SETTINGS);
            result.add(new PointWithDistance(points.get(i).latitude, points.get(i).longitude, points.get(i).speed, (float) relative, (float) (previous.absolute + relative), points.get(i).dateTime));
        }
        return result;
    }

    private static PointWithDistance calculate(final Point point, final PointWithDistance previousPoint) {
        final double relative = DISTANCE_CALCULATOR.calculateDistance(point, previousPoint, CALCULATOR_SETTINGS);
        return new PointWithDistance(point.latitude, point.longitude, point.speed, (float) relative, (float) (previousPoint.absolute + relative), point.dateTime);
    }

    @Value
    private static class Point implements LatLngAlt {
        float latitude;
        float longitude;
        int speed;
        Instant dateTime = INSTANT_ITERATOR.next();

        @Override
        public Instant getDatetime() {
            return dateTime;
        }

        @Override
        public int getAltitude() {
            return 10;
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }

    @Value
    private static class PointWithDistance implements LatLngAlt {
        float latitude;
        float longitude;
        int speed;
        float relative;
        float absolute;
        Instant dateTime;

        public String[] getCsvValues() {
            return new String[]{
                    String.valueOf(latitude),
                    String.valueOf(longitude),
                    String.valueOf(speed),
                    String.valueOf(relative),
                    String.valueOf(absolute),
                    String.valueOf(relative * 1.1),
                    String.valueOf(absolute * 1.1)
            };
        }

        @Override
        public Instant getDatetime() {
            return dateTime;
        }

        @Override
        public int getAltitude() {
            return 10;
        }

        @Override
        public boolean isValid() {
            return true;
        }
    }

    private static final class PointParser {
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;
        private static final int SPEED_INDEX = 2;

        public Point parse(final String[] properties) {
            return new Point(parseLatitude(properties), parseLongitude(properties), parseSpeed(properties));
        }

        private static float parseLatitude(final String[] properties) {
            return parseFloat(properties[LATITUDE_INDEX]);
        }

        private static float parseLongitude(final String[] properties) {
            return parseFloat(properties[LONGITUDE_INDEX]);
        }

        private static int parseSpeed(final String[] properties) {
            return parseInt(properties[SPEED_INDEX]);
        }
    }
}
