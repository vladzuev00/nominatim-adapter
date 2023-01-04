package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.model.dto.SearchingCitiesProcess;
import by.aurorasoft.nominatim.crud.service.SearchingCitiesProcessService;
import by.aurorasoft.nominatim.rest.mapper.SearchingCitiesProcessControllerMapper;
import by.aurorasoft.nominatim.rest.model.SearchingCitiesProcessResponse;
import by.aurorasoft.nominatim.rest.validator.StartSearchingCitiesRequestValidator;
import by.aurorasoft.nominatim.service.StartingSearchingCitiesProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;


import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchCityProcessController.class)
public final class SearchCityProcessControllerTest {
    private static final String URI_TEMPLATE_TO_FIND_PROCESS_BY_ID = "/searchCityTask/%d";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GeometryFactory geometryFactory;

    @MockBean
    private StartSearchingCitiesRequestValidator validator;

    @MockBean
    private StartingSearchingCitiesProcessService startingProcessService;

    @MockBean
    private SearchingCitiesProcessService processService;

    @MockBean
    private SearchingCitiesProcessControllerMapper mapper;

    @Test
    public void processShouldBeFoundById()
            throws Exception {
//        final Long givenId = 255L;
//
//        final SearchingCitiesProcess expectedFoundProcess = createProcess(givenId);
//        when(this.processService.getByIdOptional(anyLong())).thenReturn(Optional.of(expectedFoundProcess));
//
//        final SearchingCitiesProcessResponse expectedResponse = createResponse(givenId);
//        when(this.mapper.mapToResponse(any(SearchingCitiesProcess.class))).thenReturn(expectedResponse);
//
//        this.mockMvc.perform(createRequestToFindProcessByIdBuilder(givenId))
//                .andExpect(status().isOk())
//                .andExpect(content().);
        throw new RuntimeException();
    }

    private static SearchingCitiesProcess createProcess(Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }

    private static SearchingCitiesProcessResponse createResponse(Long id) {
        return SearchingCitiesProcessResponse.builder()
                .id(id)
                .build();
    }

    private static RequestBuilder createRequestToFindProcessByIdBuilder(Long id) {
        final String uri = fromUriString(format(URI_TEMPLATE_TO_FIND_PROCESS_BY_ID, id))
                .build()
                .toUriString();
        return createRequestBuilder(uri);
    }

    private static RequestBuilder createRequestBuilder(String uri) {
        return get(uri).contentType(APPLICATION_JSON);
    }
}
