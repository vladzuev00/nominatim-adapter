package by.aurorasoft.nominatim.crud.model.dto;

import by.nhorushko.crudgeneric.v2.domain.AbstractDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

import java.io.IOException;

@Value
@AllArgsConstructor
@Builder
@JsonDeserialize(using = City.CityDeserializer.class)
public class City implements AbstractDto<Long> {
    Long id;
    String name;
    Geometry geometry;

    static class CityDeserializer extends StdDeserializer<City> {
        public CityDeserializer() {
            this(null);
        }

        public CityDeserializer(Class<?> type) {
            super(type);
        }

        @Override
        public City deserialize(JsonParser jsonParser, DeserializationContext context)
                throws IOException {
            final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);
            final JsonNode featuresNode = rootNode.get("features");

            final JsonNode featureNode = featuresNode.get(0);
            final JsonNode propertiesNode = featureNode.get("properties");
            final JsonNode addressNode = propertiesNode.get("address");

            String name = null;
            final JsonNode townNode = addressNode.get("town");
            final JsonNode cityNode = addressNode.get("city");
            if (townNode != null) {
                name = townNode.asText();
            } else if(cityNode != null) {
                name = cityNode.asText();
            }

            final JsonNode bboxNode = featureNode.get("bbox");
            final double leftBottomLatitude = bboxNode.get(0).asDouble();
            final double leftBottomLongitude = bboxNode.get(1).asDouble();
            final double rightUpperLatitude = bboxNode.get(2).asDouble();
            final double rightUpperLongitude = bboxNode.get(3).asDouble();

            final GeometryFactory geometryFactory = new GeometryFactory();
            final Geometry geometry = geometryFactory.createPolygon(new Coordinate[]
                    {
                            new Coordinate(leftBottomLatitude, leftBottomLongitude),
                            new Coordinate(rightUpperLatitude, rightUpperLongitude),
                            new Coordinate(leftBottomLatitude, leftBottomLongitude)
                    });

            return new City(null, name, geometry);
        }
    }
}
