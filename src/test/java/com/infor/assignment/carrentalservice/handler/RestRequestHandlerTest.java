package com.infor.assignment.carrentalservice.handler;

import com.infor.assignment.carrentalservice.model.common.BasicRequestCriteria;
import com.infor.assignment.carrentalservice.util.MessageHeaders;
import com.infor.assignment.carrentalservice.util.MessageType;
import com.infor.assignment.carrentalservice.util.VehicleType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("Given there is Rest Request Controller")
class RestRequestHandlerTest {

    @InjectMocks
    private RestRequestHandler restRequestHandler;

    @Mock
    private CommonMessageHandlingGateway commonMessageHandlingGateway;

    @Nested
    @DisplayName("Under send message to service method,")
    class sendMessageToServiceMethod {
        @BeforeEach
        public void setUp() {

        }

        @Test
        @DisplayName("Case: message successfully send to channel")
        void testAddingAMessageIntoChannel() throws Exception {
            String requestId = UUID.randomUUID().toString();
            DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(2000L);
            BasicRequestCriteria basicRequestCriteria = new BasicRequestCriteria(requestId, MessageType.REGISTER_CAR, response, VehicleType.CAR, "plateId", "id", null);
            Mockito.doNothing().when(commonMessageHandlingGateway).send(any());
            Message<BasicRequestCriteria> basicRequestCriteriaMessage = restRequestHandler.handleRequest(MessageType.REGISTER_CAR, basicRequestCriteria);
            Assertions.assertNotNull(basicRequestCriteriaMessage.getHeaders(), "Headers can't be null");
            Assertions.assertEquals(basicRequestCriteriaMessage.getHeaders().get(MessageHeaders.REQUEST_ID), requestId, "request id can't be changed");
            Assertions.assertEquals(basicRequestCriteriaMessage.getHeaders().get(MessageHeaders.MESSAGE_TYPE), MessageType.REGISTER_CAR, "message type can't be changed");
            Assertions.assertEquals(basicRequestCriteriaMessage.getPayload(), basicRequestCriteria, "lookup criteria can't be changed");
        }
    }


}