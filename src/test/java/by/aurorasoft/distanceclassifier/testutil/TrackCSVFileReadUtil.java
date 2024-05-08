package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.nhorushko.classifieddistance.Distance;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@UtilityClass
public final class TrackCSVFileReadUtil {
    private static final String FOLDER_PATH = "./src/test/resources/tracks";
    private static final LineFactory LINE_FACTORY = new LineFactory();

    @SneakyThrows({IOException.class, CsvException.class})
    public static <C, T> T read(final String fileName,
                                final Function<Line, C> componentFactory,
                                final Function<List<C>, T> objectFactory) {
        try (final CSVReader csvReader = createCSVReader(fileName)) {
            return csvReader.readAll()
                    .stream()
                    .map(LINE_FACTORY::create)
                    .map(componentFactory)
                    .collect(collectingAndThen(toList(), objectFactory));
        }
    }

    private static CSVReader createCSVReader(final String fileName)
            throws FileNotFoundException {
        final String filePath = FOLDER_PATH + "/" + fileName;
        return new CSVReader(new FileReader(filePath));
    }

    @Value
    public static class Line {
        Coordinate coordinate;
        int speed;
        Distance gpsDistance;
        Distance odometerDistance;
    }

    private static final class LineFactory {
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;
        private static final int SPEED_INDEX = 2;
        private static final int GPS_RELATIVE_INDEX = 3;
        private static final int GPS_ABSOLUTE_INDEX = 4;
        private static final int ODOMETER_RELATIVE_INDEX = 5;
        private static final int ODOMETER_ABSOLUTE_INDEX = 6;

        public Line create(final String[] properties) {
            return new Line(
                    getCoordinate(properties),
                    getSpeed(properties),
                    getGpsDistance(properties),
                    getOdometerDistance(properties)
            );
        }

        private Coordinate getCoordinate(final String[] properties) {
            final double latitude = parseDouble(properties[LATITUDE_INDEX]);
            final double longitude = parseDouble(properties[LONGITUDE_INDEX]);
            return new Coordinate(latitude, longitude);
        }

        private int getSpeed(final String[] properties) {
            return parseInt(properties[SPEED_INDEX]);
        }

        private Distance getGpsDistance(final String[] properties) {
            return getDistance(properties, GPS_RELATIVE_INDEX, GPS_ABSOLUTE_INDEX);
        }

        private Distance getOdometerDistance(final String[] properties) {
            return getDistance(properties, ODOMETER_RELATIVE_INDEX, ODOMETER_ABSOLUTE_INDEX);
        }

        private Distance getDistance(final String[] properties, final int relativeIndex, final int absoluteIndex) {
            final double relative = parseDouble(properties[relativeIndex]);
            final double absolute = parseDouble(properties[absoluteIndex]);
            return new Distance(relative, absolute);
        }
    }
}
