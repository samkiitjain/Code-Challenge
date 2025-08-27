package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.EventData;


public interface EventRegisterImpl {
    EventRegisterModel fetchEventsForTimeStamp(int event, Long timestamp);
    EventRegisterModel saveEvent(EventData request);
}
