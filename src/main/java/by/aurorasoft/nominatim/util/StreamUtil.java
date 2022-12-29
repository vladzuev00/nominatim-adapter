package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;

import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@UtilityClass
public class StreamUtil {
    public static <T> Stream<T> asStream(Iterator<T> source) {
        return asStream(source, false);
    }

    public static <T> Stream<T> asStream(Iterator<T> source, boolean parallel) {
        final Iterable<T> iterable = () -> source;
        return stream(iterable.spliterator(), parallel);
    }

    public static <T> Stream<Stream<T>> split(Stream<T> stream, int amountOfElementsInPart) {
        final Iterator<T> iterator = stream.iterator();
        final Stream.Builder<Stream<T>> resultStreamBuilder = Stream.builder();
        Stream.Builder<T> subStreamBuilder;
        while (iterator.hasNext()) {
            subStreamBuilder = Stream.builder();
            for (int i = 0; i < amountOfElementsInPart && iterator.hasNext(); i++) {
                subStreamBuilder.accept(iterator.next());
            }
            resultStreamBuilder.accept(subStreamBuilder.build());
        }
        return resultStreamBuilder.build();
    }
}
