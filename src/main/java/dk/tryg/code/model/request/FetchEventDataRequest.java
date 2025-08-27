package dk.tryg.code.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FetchEventDataRequest {
    private int eventKey;
    private long eventTimestamp;
}
