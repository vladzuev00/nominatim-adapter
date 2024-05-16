//package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator;
//
//import by.aurorasoft.distanceclassifier.model.Coordinate;
//import by.aurorasoft.distanceclassifier.model.TrackPoint;
//import by.nhorushko.classifieddistance.Distance;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.apache.commons.collections4.IteratorUtils.toList;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public final class SignificantTrackPointIteratorTest {
//
//    @ParameterizedTest
//    @MethodSource("providePointsAndThresholdAndExpected")
//    public void pointsShouldBeIterated(final List<TrackPoint> givenPoints,
//                                       final double givenGpsThreshold,
//                                       final List<TrackPoint> expected) {
//        final var givenIterator = new SignificantTrackPointIterator(givenPoints, givenGpsThreshold);
//        final List<TrackPoint> actual = toList(givenIterator);
//        assertEquals(expected, actual);
//    }
//
//    private static Stream<Arguments> providePointsAndThresholdAndExpected() {
//        return Stream.of(
//                Arguments.of(
//                        List.of(
//                                createPoint(new Coordinate(1, 1), new Distance(100, 150), new Distance(110, 160)),
//                                createPoint(new Coordinate(2, 2), new Distance(50, 200), new Distance(60, 220)),
//                                createPoint(new Coordinate(3, 3), 185, 195),
//                                createPoint(new Coordinate(4, 4), 190, 200),
//                                createPoint(new Coordinate(5, 5), 250, 260),
//                                createPoint(new Coordinate(6, 6), 335, 345),
//                                createPoint(new Coordinate(7, 7), 380, 390),
//                                createPoint(new Coordinate(8, 8), 390, 400),
//                                createPoint(new Coordinate(10, 10), 480, 490),
//                                createPoint(new Coordinate(11, 11), 550, 560),
//                                createPoint(new Coordinate(12, 12), 560, 570),
//                                createPoint(new Coordinate(13, 13), 570, 580),
//                                createPoint(new Coordinate(14, 14), 580, 590),
//                                createPoint(new Coordinate(15, 15), 650, 660),
//                                createPoint(new Coordinate(16, 16), 900, 910),
//                                createPoint(new Coordinate(17, 17), 910, 920),
//                                createPoint(new Coordinate(18, 18), 920, 930),
//                                createPoint(new Coordinate(19, 19), 930, 940),
//                                createPoint(new Coordinate(20, 20), 940, 950),
//                                createPoint(new Coordinate(21, 21), 1010, 1020),
//                                createPoint(new Coordinate(22, 22), 1020, 1030),
//                                createPoint(new Coordinate(23, 23), 1030, 1040),
//                                createPoint(new Coordinate(24, 24), 1040, 1050),
//                                createPoint(new Coordinate(25, 25), 1200, 1210),
//                                createPoint(new Coordinate(26, 26), 1500, 1510),
//                                createPoint(new Coordinate(27, 27), 1550, 1560)
//                        ),
//                        300,
//                        List.of(
//                                createPoint(new Coordinate(1, 1), 100, 110),
//                                createPoint(new Coordinate(10, 10), 480, 490),
//                                createPoint(new Coordinate(16, 16), 900, 910),
//                                createPoint(new Coordinate(25, 25), 1200, 1210),
//                                createPoint(new Coordinate(26, 26), 1500, 1510),
//                                createPoint(new Coordinate(27, 27), 1550, 1560)
//                        )
//                )
////                Arguments.of(
////                        List.of(
////                                createPoint(new Coordinate(1, 1), 100),
////                                createPoint(new Coordinate(2, 2), 150),
////                                createPoint(new Coordinate(3, 3), 185),
////                                createPoint(new Coordinate(4, 4), 190),
////                                createPoint(new Coordinate(5, 5), 250),
////                                createPoint(new Coordinate(6, 6), 335),
////                                createPoint(new Coordinate(7, 7), 380),
////                                createPoint(new Coordinate(8, 8), 390),
////                                createPoint(new Coordinate(10, 10), 480),
////                                createPoint(new Coordinate(11, 11), 550),
////                                createPoint(new Coordinate(12, 12), 560),
////                                createPoint(new Coordinate(13, 13), 570),
////                                createPoint(new Coordinate(14, 14), 580)
////                        ),
////                        0,
////                        List.of(
////                                createPoint(new Coordinate(1, 1), 100),
////                                createPoint(new Coordinate(2, 2), 150),
////                                createPoint(new Coordinate(3, 3), 185),
////                                createPoint(new Coordinate(4, 4), 190),
////                                createPoint(new Coordinate(5, 5), 250),
////                                createPoint(new Coordinate(6, 6), 335),
////                                createPoint(new Coordinate(7, 7), 380),
////                                createPoint(new Coordinate(8, 8), 390),
////                                createPoint(new Coordinate(10, 10), 480),
////                                createPoint(new Coordinate(11, 11), 550),
////                                createPoint(new Coordinate(12, 12), 560),
////                                createPoint(new Coordinate(13, 13), 570),
////                                createPoint(new Coordinate(14, 14), 580)
////                        )
////                ),
////                Arguments.of(emptyList(), 100, emptyList())
//        );
//    }
//
//    private static TrackPoint createPoint(final Coordinate coordinate,
//                                          final Distance gpsDistance,
//                                          final Distance odometerDistance) {
//        return TrackPoint.builder()
//                .coordinate(coordinate)
//                .gpsDistance(gpsDistance)
//                .odometerDistance(odometerDistance)
//                .build();
//    }
//}
