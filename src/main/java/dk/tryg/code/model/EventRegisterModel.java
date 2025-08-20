package dk.tryg.code.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "event_register")
public class EventRegisterModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID", nullable = false)
    private int eventID;

    @Column(name = "EVENT_KEY", nullable = false)
    private int eventKey;

    @Column(name = "EVENT", nullable = false)
    private String event;

    @Column(name = "EVENT_TIME", nullable = false)
    private Long eventTime;
}
