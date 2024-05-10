package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@UtilityClass
public final class TrackCSVFileReadUtil {
    private static final String FOLDER_PATH = "./src/test/resources/tracks";
    private static final PointFactory POINT_FACTORY = new PointFactory();

    @SneakyThrows({IOException.class, CsvException.class})
    public static Track read(final String fileName) {
        try (final CSVReader csvReader = createCSVReader(fileName)) {
            return csvReader.readAll()
                    .stream()
                    .map(POINT_FACTORY::create)
                    .collect(collectingAndThen(toList(), Track::new));
        }
    }

    private static CSVReader createCSVReader(final String fileName)
            throws FileNotFoundException {
        final String filePath = FOLDER_PATH + "/" + fileName;
        return new CSVReader(new FileReader(filePath));
    }

    private static final class PointFactory {
        private static final int LATITUDE_INDEX = 0;
        private static final int LONGITUDE_INDEX = 1;
        private static final int SPEED_INDEX = 2;
        private static final int GPS_RELATIVE_INDEX = 3;
        private static final int GPS_ABSOLUTE_INDEX = 4;
        private static final int ODOMETER_RELATIVE_INDEX = 5;
        private static final int ODOMETER_ABSOLUTE_INDEX = 6;

        public TrackPoint create(final String[] properties) {
            return new TrackPoint(
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
