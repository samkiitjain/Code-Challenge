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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        int key = 1;
        long timestamp = 100;
        String value = "Event1";

        putValue(key, timestamp, value);
        ResponseEntity<FetchEventResponse> getResponse = callFetchEventAPI(key, timestamp);

        Assertions.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(getResponse.getBody()).getEventName()).isEqualTo(value);
    }

    @Test
    void testGetBeforeEntryExists() throws JsonProcessingException {
        ResponseEntity<FetchEventResponse> response = callFetchEventAPI(2, 10);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testMultipleTimestampsReturnsLatestValue() throws JsonProcessingException {
        // Insert two values
        putValue(3, 100, "Event3");
        putValue(3, 200, "Event4");

        // GET at timestamp=150 should return value3
        ResponseEntity<FetchEventResponse> response = callFetchEventAPI(3, 150);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getEventName()).isEqualTo("Event3");

        // GET at timestamp=250 should return value4
        ResponseEntity<FetchEventResponse> response2 = callFetchEventAPI(3, 250);
        Assertions.assertThat(Objects.requireNonNull(response2.getBody()).getEventName()).isEqualTo("Event4");
    }

    @Test
    void testConcurrentInserts() throws Exception {
        int threads = 10;
        int key = 100;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 1; i < threads; i++) {
            final int index = i;
            tasks.add(() -> {
                putValue(key, index * 10, "Event" + index);
                return null;
            });
        }

        executor.invokeAll(tasks);
        executor.shutdown();

        ResponseEntity<FetchEventResponse> response = callFetchEventAPI(100, 90);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody().getEventName()).isEqualTo("Event9");
    }

    private ResponseEntity<FetchEventResponse> callFetchEventAPI(int key, long timestamp) throws JsonProcessingException {
        HttpEntity<String> getEventRequest = buildGetEventRequest(key, timestamp);
        ResponseEntity<FetchEventResponse> getResponse = restTemplate.exchange(getBaseUrl() + "/api/event/fetchEvent", HttpMethod.GET, getEventRequest, FetchEventResponse.class);
        return getResponse;
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

    private HttpEntity<String> buildGetEventRequest(int eventKey, long timestamp) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();

        FetchEventDataRequest fetchEvent = new FetchEventDataRequest();
        fetchEvent.setEventKey(eventKey);
        fetchEvent.setEventTimestamp(timestamp);

        String getEventPayload = mapper.writeValueAsString(fetchEvent);
        HttpEntity<String> request = new HttpEntity<>(getEventPayload, headers);
        return request;
    }
}
