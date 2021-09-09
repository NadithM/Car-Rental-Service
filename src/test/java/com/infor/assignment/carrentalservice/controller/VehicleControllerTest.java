package com.infor.assignment.carrentalservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infor.assignment.carrentalservice.annotation.Brand;
import com.infor.assignment.carrentalservice.handler.RestRequestHandler;
import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.model.vehicle.*;
import com.infor.assignment.carrentalservice.util.Constants;
import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import com.infor.assignment.carrentalservice.util.VehicleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CarController.class)
@ActiveProfiles("test")
@DisplayName("Given there is Car Controller")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestRequestHandler restRequestHandler;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Under Client Error Scenarios,")
    class ClientErrors {
        public final String ERROR_INFO = "errorInfo";
        public final String LOCATION = "location";
        public final String STATUS = "status";

        @BeforeEach
        public void setUp() {

        }

        @Test
        @DisplayName("Case: Mandatory field is missing in the request")
        void testMandatoryFieldIsMissing() throws Exception {
            CarRegisterRequest carRegisterRequest = new CarRegisterRequest();
            String contentAsString = call(carRegisterRequest, status().isBadRequest());
            Object response = objectMapper.readValue(contentAsString, Object.class);
            Assertions.assertEquals("Plate Number can't be null or empty", ((LinkedHashMap) response).get(ERROR_INFO), "errorInfo field is Incorrect");
            Assertions.assertEquals("plateNumber", ((LinkedHashMap) response).get(LOCATION), "location field is Incorrect");
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), ((LinkedHashMap) response).get(STATUS), "status field is Incorrect");
        }

        @Test
        @DisplayName("Case: Invalid value for optional field in request payload")
        void InvalidValueInRequestPayload() throws Exception {
            CarRegisterRequest carRegisterRequest = new CarRegisterRequest("plateNumber", null, "LP", null, null, null);
            String contentAsString = call(carRegisterRequest, status().isBadRequest());
            Object response = objectMapper.readValue(contentAsString, Object.class);
            Assertions.assertEquals("Invalid fuelType : LP. Valid fuelTypes are [PETROL, DIESEL, LPG, ELECTRIC, HYBRID]", ((LinkedHashMap) response).get(ERROR_INFO), "errorInfo field is Incorrect");
            Assertions.assertEquals("typeOfFuel", ((LinkedHashMap) response).get(LOCATION), "location field is Incorrect");
            Assertions.assertEquals(String.valueOf(HttpStatus.BAD_REQUEST.value()), ((LinkedHashMap) response).get(STATUS), "status field is Incorrect");
        }

        private String call(CarRegisterRequest carRegisterRequest, ResultMatcher statusResultMatchers) throws Exception {
            String jsonStr = objectMapper.writeValueAsString(carRegisterRequest);
            MvcResult result = mockMvc.perform(
                            post("/car-rental-service/v1/vehicles/cars")
                                    .contextPath("/car-rental-service")
                                    .content(jsonStr)
                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(statusResultMatchers)
                    .andReturn();

            return result.getResponse().getContentAsString();
        }


    }

    @Nested
    @DisplayName("Under Success Scenarios,")
    class SuccessFlows {
        @BeforeEach
        public void setUp() {

        }

        @Test
        @DisplayName("Case: when registering a car")
        void testRegisterACar() throws Exception {
            CarRegisterRequest carRegisterRequest = new CarRegisterRequest("plateNumber", "AUDI", "LPG", "MANUAL", Arrays.asList(Feature.GPS.name(), Feature.PET_FRIENDLY.name()), "SEDAN");
            String requestId = UUID.randomUUID().toString();
            DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(2000L);
            BasicRequestCriteria basicRequestCriteria = new BasicRequestCriteria(requestId, MessageType.REGISTER_CAR, response, VehicleType.CAR, "plateId", "id", null);
            when(restRequestHandler.handleRequest(any(), any())).thenReturn(
                    MessageBuilder
                            .withPayload(basicRequestCriteria)
                            .setHeader(MessageHeaders.REQUEST_ID, basicRequestCriteria.getRequestId())
                            .setHeader(MessageHeaders.MESSAGE_TYPE, basicRequestCriteria.getMessageType())
                            .build()
            );

            String jsonStr = objectMapper.writeValueAsString(carRegisterRequest);
            MvcResult result = mockMvc.perform(
                            post("/car-rental-service/v1/vehicles/cars")
                                    .contextPath("/car-rental-service")
                                    .content(jsonStr)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(request().asyncStarted())
                    .andReturn();
            response.setResult(ResponseEntity.ok(new Car("id", "plateId", VehicleType.CAR, Brand.AUDI, Fuel.LPG, Transmission.MANUAL, Arrays.asList(Feature.GPS, Feature.PET_FRIENDLY), CarBodyConfiguration.SEDAN)));
            result.getAsyncResult(5000L);
            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("id"))
                    .andExpect(jsonPath("$.features").isArray())
                    .andExpect(jsonPath("$.features.length()").value(2));
        }
    }

    @Nested
    @DisplayName("Under Server Error Scenarios,")
    class ServerErrorFlows {
        @BeforeEach
        public void setUp() {

        }

        @Test
        @DisplayName("Case: when there is an checked error while processing request")
        void testCheckedException() throws Exception {
            CarRegisterRequest carRegisterRequest = new CarRegisterRequest("plateNumber", "AUDI", "LPG", "MANUAL", Arrays.asList(Feature.GPS.name(), Feature.PET_FRIENDLY.name()), "SEDAN");
            String requestId = UUID.randomUUID().toString();
            DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(2000L);
            BasicRequestCriteria basicRequestCriteria = new BasicRequestCriteria(requestId, MessageType.REGISTER_CAR, response, VehicleType.CAR, "plateId", "id", null);
            when(restRequestHandler.handleRequest(any(), any())).thenReturn(
                    MessageBuilder
                            .withPayload(basicRequestCriteria)
                            .setHeader(MessageHeaders.REQUEST_ID, basicRequestCriteria.getRequestId())
                            .setHeader(MessageHeaders.MESSAGE_TYPE, basicRequestCriteria.getMessageType())
                            .build()
            );

            String jsonStr = objectMapper.writeValueAsString(carRegisterRequest);
            MvcResult result = mockMvc.perform(
                            post("/car-rental-service/v1/vehicles/cars")
                                    .contextPath("/car-rental-service")
                                    .content(jsonStr)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(request().asyncStarted())
                    .andReturn();
            Map<String, String> errorMap = new HashMap<>();
            //default error response
            errorMap.put(Constants.ERROR_INFO, "This is error Message");
            errorMap.put(Constants.STATUS, String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            response.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap));

            result.getAsyncResult(5000L);
            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorInfo").value("This is error Message"))
                    .andExpect(jsonPath("$.status").value(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())));
        }

        @Test
        @DisplayName("Case: when there is an request timeout while processing")
        void testTimeoutRequest() throws Exception {
            CarRegisterRequest carRegisterRequest = new CarRegisterRequest("plateNumber", "AUDI", "LPG", "MANUAL", Arrays.asList(Feature.GPS.name(), Feature.PET_FRIENDLY.name()), "SEDAN");
            String requestId = UUID.randomUUID().toString();
            DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(2000L);
            BasicRequestCriteria basicRequestCriteria = new BasicRequestCriteria(requestId, MessageType.REGISTER_CAR, response, VehicleType.CAR, "plateId", "id", null);
            when(restRequestHandler.handleRequest(any(), any())).thenReturn(
                    MessageBuilder
                            .withPayload(basicRequestCriteria)
                            .setHeader(MessageHeaders.REQUEST_ID, basicRequestCriteria.getRequestId())
                            .setHeader(MessageHeaders.MESSAGE_TYPE, basicRequestCriteria.getMessageType())
                            .build()
            );

            String jsonStr = objectMapper.writeValueAsString(carRegisterRequest);
            MvcResult result = mockMvc.perform(
                            post("/car-rental-service/v1/vehicles/cars")
                                    .contextPath("/car-rental-service")
                                    .content(jsonStr)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(request().asyncStarted())
                    .andReturn();
            Map<String, String> errorMap = new HashMap<>();
            //default error response
            errorMap.put(Constants.ERROR_INFO, "request timeout");
            errorMap.put(Constants.STATUS, String.valueOf(HttpStatus.REQUEST_TIMEOUT.value()));
            response.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorMap));

            result.getAsyncResult(5000L);
            mockMvc
                    .perform(asyncDispatch(result))
                    .andExpect(status().isRequestTimeout())
                    .andExpect(jsonPath("$.errorInfo").value("request timeout"))
                    .andExpect(jsonPath("$.status").value(String.valueOf(HttpStatus.REQUEST_TIMEOUT.value())));
        }
    }

}

