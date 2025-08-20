package dk.tryg.code.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EventDataRequest {
    private int eventKey;
    private long eventTimestamp;
    private String eventName;
}
