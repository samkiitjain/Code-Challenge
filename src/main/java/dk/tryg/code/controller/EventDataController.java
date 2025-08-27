package dk.tryg.code.controller;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.EventData;
import dk.tryg.code.model.request.FetchEventDataRequest;
import dk.tryg.code.model.response.FetchEventResponse;
import dk.tryg.code.service.EventRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/event")
public class EventDataController {

    private Logger logger = Logger.getLogger(EventDataController.class.getName());

    @Autowired
    private EventRegisterService eventRegisterService;

    @GetMapping(path="/fetchEvent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getEvent(@RequestBody FetchEventDataRequest request) {
        EventRegisterModel event = eventRegisterService.fetchEventsForTimeStamp(request.getEventKey(), request.getEventTimestamp());
        if(Objects.isNull(event)) {
            return ResponseEntity.noContent().build();
        }
            return ResponseEntity.ok(new FetchEventResponse(event.getEvent()));
    }

    @PutMapping(path="/putEvent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity putEvent(@RequestBody EventData request) {
        EventRegisterModel events = eventRegisterService.saveEvent(request);
        return ResponseEntity.ok(events);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        logger.log(new LogRecord(Level.SEVERE, ex.getMessage()));
    }
}
