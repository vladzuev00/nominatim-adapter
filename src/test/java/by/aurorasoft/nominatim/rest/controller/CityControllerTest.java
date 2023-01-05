package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.crud.service.CityService;
import by.aurorasoft.nominatim.rest.mapper.CityControllerMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(CityControllerTest.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService service;

    @MockBean
    private CityControllerMapper mapper;


}
