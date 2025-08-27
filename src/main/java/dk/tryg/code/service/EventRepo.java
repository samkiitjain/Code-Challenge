package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;



public interface EventRepo extends JpaRepository<EventRegisterModel, Integer> {

    /*SELECT TOP 1 events FROM EventRegisterModel
    events WHERE events.eventKey = :eventKey AND
    (events.eventTime < :timestamp OR events.eventTime = :timestamp)
    ORDER BY events.eventTime DESC")*/

    EventRegisterModel findFirstByEventKeyAndEventTimeLessThanEqualOrderByEventTimeDesc(int eventKey, Long timestamp);
}