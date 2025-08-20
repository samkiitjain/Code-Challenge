package dk.tryg.code.service;

import dk.tryg.code.model.EventRegisterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface EventRepo extends JpaRepository<EventRegisterModel, Integer> {

    @Query("SELECT e FROM EventRegisterModel e WHERE e.eventKey = :eventKey AND (e.eventTime < :timestamp OR e.eventTime = :timestamp) ORDER BY e.eventTime DESC")
    List<EventRegisterModel> findEvents(int eventKey, Long timestamp);
}