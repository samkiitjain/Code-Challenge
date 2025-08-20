package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.request.EventDataRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventRegisterService implements EventRegisterImpl{

    @Autowired
    private EventRepo repository;

    /**
     * @param event
     * @param eventTime
     * @return event
     */
    @Override
    @Transactional(readOnly = true)
    public List<EventRegisterModel> fetchEventsForTimeStamp(int event, Long eventTime) {
       List<EventRegisterModel> events = repository.findEvents(event, eventTime);
       return events;
    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional
    public EventRegisterModel saveEvent(EventDataRequest request) {
        EventRegisterModel model = new EventRegisterModel();
        model.setEventKey(request.getEventKey());
        model.setEvent(request.getEventName());
        model.setEventTime(request.getEventTimestamp());
        return repository.save(model);
    }
}
