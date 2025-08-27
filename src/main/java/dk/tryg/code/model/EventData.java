package dk.tryg.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private int eventKey;
    private long eventTimestamp;
    private String eventName;

    @Override
    public String toString() {
        return "{" +
                "eventKey':" + eventKey +
                ",'eventTimestamp':" + eventTimestamp +
                ",'eventName':" + eventName +
                "}";
    }

}
