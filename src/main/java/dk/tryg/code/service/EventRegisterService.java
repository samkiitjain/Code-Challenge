package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import dk.tryg.code.model.EventData;
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
    public EventRegisterModel fetchEventsForTimeStamp(int event, Long eventTime) {
       EventRegisterModel eventData = repository.findFirstByEventKeyAndEventTimeLessThanEqualOrderByEventTimeDesc(event, eventTime);
       return eventData;
    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional
    public EventRegisterModel saveEvent(EventData request) {
        EventRegisterModel model = new EventRegisterModel();
        model.setEventKey(request.getEventKey());
        model.setEvent(request.getEventName());
        model.setEventTime(request.getEventTimestamp());
        return repository.save(model);
    }
}
