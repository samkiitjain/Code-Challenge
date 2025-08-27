package dk.tryg.code.challenge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.tryg.code.model.EventData;
import dk.tryg.code.model.request.FetchEventDataRequest;
import dk.tryg.code.model.response.FetchEventResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void testPutAndGetValue() throws JsonProcessingException {
        // Arrange
        int key = 1;
        long timestamp = 100;
        String value = "value1";

        EventData requestEvent = new EventData();
        requestEvent.setEventKey(key);
        requestEvent.setEventTimestamp(timestamp);
        requestEvent.setEventName(value);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(requestEvent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        // Act: PUT value
        ResponseEntity<Void> putResponse = restTemplate.exchange(getBaseUrl() + "/api/event/putEvent", HttpMethod.PUT, request, Void.class);

        // Assert PUT success
        Assertions.assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        FetchEventDataRequest fetchEvent = new FetchEventDataRequest();
        fetchEvent.setEventKey(key);
        fetchEvent.setEventTimestamp(timestamp);

        String getEventPayload = mapper.writeValueAsString(fetchEvent);
        HttpEntity<String> getEventRequest = new HttpEntity<>(getEventPayload, headers);
        // Act: GET value at timestamp
        ResponseEntity<FetchEventResponse> getResponse = restTemplate.exchange(getBaseUrl() + "/api/event/fetchEvent",
                                                                                    HttpMethod.GET, getEventRequest, FetchEventResponse.class);

        // Assert GET success
        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(getResponse.getBody().getEventName()).isEqualTo(value);
    }

    @Test
    void testGetBeforeEntryExists() throws JsonProcessingException {
        FetchEventDataRequest fetchEvent = new FetchEventDataRequest();
        fetchEvent.setEventKey(2);
        fetchEvent.setEventTimestamp(10);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        String getEventPayload = mapper.writeValueAsString(fetchEvent);
        HttpEntity<String> request = new HttpEntity<>(getEventPayload, headers);

        ResponseEntity<FetchEventResponse> response = restTemplate.exchange(getBaseUrl() + "/api/event/fetchEvent",
                HttpMethod.GET, request, FetchEventResponse.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testMultipleTimestampsReturnsLatestValue() throws JsonProcessingException {
        // Insert two values
        putValue(3, 100, "value3");
        putValue(3, 200, "value4");

        //Setup common objects
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        // GET at timestamp=150 should return value3
        FetchEventDataRequest fetchEvent = new FetchEventDataRequest();
        fetchEvent.setEventKey(3);
        fetchEvent.setEventTimestamp(150);

        String getEventPayload = mapper.writeValueAsString(fetchEvent);
        HttpEntity<String> request = new HttpEntity<>(getEventPayload, headers);
        ResponseEntity<FetchEventResponse> response = restTemplate.exchange(getBaseUrl() + "/api/event/fetchEvent", HttpMethod.GET, request, FetchEventResponse.class);
        Assertions.assertThat(response.getBody().getEventName()).isEqualTo("value3");

        // GET at timestamp=250 should return value4
        FetchEventDataRequest fetchEvent2 = new FetchEventDataRequest();
        fetchEvent2.setEventKey(3);
        fetchEvent2.setEventTimestamp(250);

        String getEventPayload2 = mapper.writeValueAsString(fetchEvent2);
        HttpEntity<String> request2 = new HttpEntity<>(getEventPayload2, headers);

        ResponseEntity<FetchEventResponse> response2 = restTemplate.exchange(getBaseUrl() + "/api/event/fetchEvent", HttpMethod.GET, request2, FetchEventResponse.class);
        Assertions.assertThat(response2.getBody().getEventName()).isEqualTo("value4");
    }

    private void putValue(int key, long timestamp, String value) throws JsonProcessingException {
        EventData requestEvent = new EventData();
        requestEvent.setEventKey(key);
        requestEvent.setEventTimestamp(timestamp);
        requestEvent.setEventName(value);

        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(requestEvent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/api/event/putEvent", HttpMethod.PUT, request, Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
