package dk.tryg.code.controller;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.request.EventDataRequest;
import dk.tryg.code.service.EventRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/event")
public class EventDataController {

    private Logger logger = Logger.getLogger(EventDataController.class.getName());

    @Autowired
    private EventRegisterService eventRegisterService;

    @GetMapping(path="/fetchEvent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getEvent(@RequestBody EventDataRequest request) {
        List<EventRegisterModel> events = eventRegisterService.fetchEventsForTimeStamp(request.getEventKey(), request.getEventTimestamp());
        if(events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events.get(0).getEvent());
    }

    @PutMapping(path="/putEvent", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity putEvent(@RequestBody EventDataRequest request) {
        EventRegisterModel events = eventRegisterService.saveEvent(request);
        return ResponseEntity.ok(events);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public void handleException() {
        logger.log(Level.SEVERE, "Issue Occurred");
    }
}
