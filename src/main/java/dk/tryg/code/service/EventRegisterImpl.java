package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.request.EventDataRequest;

import java.util.List;
import java.util.Optional;

public interface EventRegisterImpl {
    List<EventRegisterModel> fetchEventsForTimeStamp(int event, Long timestamp);
    EventRegisterModel saveEvent(EventDataRequest request);
}
