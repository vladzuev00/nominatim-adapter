package by.aurorasoft.nominatim.service.searchcity;

import by.aurorasoft.nominatim.base.AbstractContextTest;
import by.aurorasoft.nominatim.crud.model.dto.AreaCoordinate;
import by.aurorasoft.nominatim.crud.model.dto.Coordinate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;

import static by.aurorasoft.nominatim.util.StreamUtil.asStream;
import static java.lang.Class.forName;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public final class StartingSearchingCitiesProcessServiceIT extends AbstractContextTest {
    private static final String CLASS_NAME_AREA_ITERATOR
            = "by.aurorasoft.nominatim.service.searchcity.StartingSearchingCitiesProcessService$AreaIterator";
    private static final String CLASS_NAME_SUB_AREA_ITERATOR
            = "by.aurorasoft.nominatim.service.searchcity.StartingSearchingCitiesProcessService$SubAreaIterator";

    @Autowired
    private StartingSearchingCitiesProcessService service;

    @Test
    public void areaShouldBeIteratedByAreaIteratorFirstCase()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1., 1.),
                new Coordinate(5., 4.)
        );
        final double givenSearchStep = 1;
        final Iterator<Coordinate> givenAreaIterator = createAreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<Coordinate> actual = asStream(givenAreaIterator)
                .collect(toList());
        final List<Coordinate> expected = List.of(
                new Coordinate(1., 1.),
                new Coordinate(2., 1.),
                new Coordinate(3., 1.),
                new Coordinate(4., 1.),
                new Coordinate(5., 1.),

                new Coordinate(1., 2.),
                new Coordinate(2., 2.),
                new Coordinate(3., 2.),
                new Coordinate(4., 2.),
                new Coordinate(5., 2.),

                new Coordinate(1., 3.),
                new Coordinate(2., 3.),
                new Coordinate(3., 3.),
                new Coordinate(4., 3.),
                new Coordinate(5., 3.),

                new Coordinate(1., 4.),
                new Coordinate(2., 4.),
                new Coordinate(3., 4.),
                new Coordinate(4., 4.),
                new Coordinate(5., 4.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIteratedByAreaIteratorSecondCase()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1., 1.),
                new Coordinate(5.5, 4.5)
        );
        final double givenSearchStep = 1;
        final Iterator<Coordinate> givenAreaIterator = createAreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<Coordinate> actual = asStream(givenAreaIterator)
                .collect(toList());
        final List<Coordinate> expected = List.of(
                new Coordinate(1., 1.),
                new Coordinate(2., 1.),
                new Coordinate(3., 1.),
                new Coordinate(4., 1.),
                new Coordinate(5., 1.),

                new Coordinate(1., 2.),
                new Coordinate(2., 2.),
                new Coordinate(3., 2.),
                new Coordinate(4., 2.),
                new Coordinate(5., 2.),

                new Coordinate(1., 3.),
                new Coordinate(2., 3.),
                new Coordinate(3., 3.),
                new Coordinate(4., 3.),
                new Coordinate(5., 3.),

                new Coordinate(1., 4.),
                new Coordinate(2., 4.),
                new Coordinate(3., 4.),
                new Coordinate(4., 4.),
                new Coordinate(5., 4.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void areaShouldBeIteratedByAreaIteratorThirdCase()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1., 1.),
                new Coordinate(5., 4.)
        );
        final double givenSearchStep = 10;
        final Iterator<Coordinate> givenAreaIterator = createAreaIterator(givenAreaCoordinate, givenSearchStep);

        final List<Coordinate> actual = asStream(givenAreaIterator)
                .collect(toList());
        final List<Coordinate> expected = List.of(new Coordinate(1., 1.));
        assertEquals(expected, actual);
    }

    @Test
    public void subAreaShouldBeIteratedBySubAreaIteratorFirstCase()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1., 1.),
                new Coordinate(5., 4.)
        );
        final double givenSearchStep = 1;
        final Iterator<Coordinate> givenAreaIterator = createAreaIterator(givenAreaCoordinate, givenSearchStep);
        final Iterator<Coordinate> givenSubAreaIterator = this.createSubAreaIterator(givenAreaIterator);

        final List<Coordinate> actual = asStream(givenSubAreaIterator)
                .collect(toList());
        final List<Coordinate> expected = List.of(
                new Coordinate(1., 1.),
                new Coordinate(2., 1.),
                new Coordinate(3., 1.),
                new Coordinate(4., 1.),
                new Coordinate(5., 1.),

                new Coordinate(1., 2.),
                new Coordinate(2., 2.),
                new Coordinate(3., 2.),
                new Coordinate(4., 2.),
                new Coordinate(5., 2.)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void subAreaShouldBeIteratedBySubAreaIteratorSecondCase() throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1., 1.),
                new Coordinate(5., 4.)
        );
        final double givenSearchStep = 10;
        final Iterator<Coordinate> givenAreaIterator = createAreaIterator(givenAreaCoordinate, givenSearchStep);
        final Iterator<Coordinate> givenSubAreaIterator = this.createSubAreaIterator(givenAreaIterator);

        final List<Coordinate> actual = asStream(givenSubAreaIterator)
                .collect(toList());
        final List<Coordinate> expected = List.of(new Coordinate(1., 1.));
        assertEquals(expected, actual);
    }



    private static Iterator<Coordinate> createAreaIterator(AreaCoordinate areaCoordinate, double searchStep)
            throws Exception {
        return createCoordinateIterator(
                CLASS_NAME_AREA_ITERATOR,
                new Class<?>[]{
                        AreaCoordinate.class, double.class
                },
                new Object[]{
                        areaCoordinate, searchStep
                }
        );
    }

    private Iterator<Coordinate> createSubAreaIterator(Iterator<Coordinate> areaIterator)
            throws Exception {
        final Class<? extends Iterator<Coordinate>> classAreaIterator
                = findClassCoordinateIterator(CLASS_NAME_AREA_ITERATOR);
        return createCoordinateIterator(
                CLASS_NAME_SUB_AREA_ITERATOR,
                new Class<?>[]{
                        StartingSearchingCitiesProcessService.class,
                        classAreaIterator
                },
                new Object[]{
                        this.service,
                        areaIterator
                }
        );
    }

    private static Iterator<Coordinate> createCoordinateIterator(String className,
                                                                 Class<?>[] constructorArgumentsTypes,
                                                                 Object[] constructorArguments)
            throws Exception {
        final Class<? extends Iterator<Coordinate>> classCoordinateIterator = findClassCoordinateIterator(className);
        final Constructor<? extends Iterator<Coordinate>> constructorCoordinateIterator
                = classCoordinateIterator.getConstructor(constructorArgumentsTypes);
        return constructorCoordinateIterator.newInstance(constructorArguments);
    }

    @SuppressWarnings("all")
    private static Class<? extends Iterator<Coordinate>> findClassCoordinateIterator(String className)
            throws Exception {
        return (Class<? extends Iterator<Coordinate>>) forName(className);
    }
}
