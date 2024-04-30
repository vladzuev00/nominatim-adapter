package by.aurorasoft.distanceclassifier;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.Value;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        final String filePath = "./src/test/resources/tracks/unit_460_13000.csv";
        final List<Line> lines = readLines(filePath);
        writeWithoutAltitude(lines, filePath);
    }

    private static List<Line> readLines(final String filePath) {
        final LineParser lineParser = new LineParser();
        try (final CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            return csvReader.readAll()
                    .stream()
                    .map(lineParser::parse)
                    .toList();
        } catch (final IOException | CsvException cause) {
            throw new RuntimeException(cause);
        }
    }

    private static void writeWithoutAltitude(final List<Line> lines, final String filePath) {
        try (final CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath, false))) {
            csvWriter.writeAll(lines.stream().map(Line::getValuesExceptAltitude).toList());
        } catch (final IOException cause) {
            throw new RuntimeException(cause);
        }
    }

    @Value
    private static class Line {
        String latitude;
        String longitude;
        String altitude;
        String speed;

        public String[] getValuesExceptAltitude() {
            return new String[]{
                    latitude, longitude, speed
            };
        }
    }

    private static final class LineParser {
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;
        private static final int ALTITUDE_INDEX = 2;
        private static final int SPEED_INDEX = 3;

        public Line parse(final String[] properties) {
            return new Line(
                    parseLatitude(properties),
                    parseLongitude(properties),
                    parseAltitude(properties),
                    parseSpeed(properties)
            );
        }

        private static String parseLatitude(final String[] properties) {
            return properties[LATITUDE_INDEX];
        }

        private static String parseLongitude(final String[] properties) {
            return properties[LONGITUDE_INDEX];
        }

        private static String parseAltitude(final String[] properties) {
            return properties[ALTITUDE_INDEX];
        }

        private static String parseSpeed(final String[] properties) {
            return properties[SPEED_INDEX];
        }
    }
}
