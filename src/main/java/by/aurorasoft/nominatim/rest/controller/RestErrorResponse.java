package by.aurorasoft.nominatim.rest.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Value
public class RestErrorResponse {
    HttpStatus httpStatus;
    String message;

    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH-mm-ss")
    LocalDateTime dateTime;
}
