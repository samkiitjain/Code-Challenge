CREATE TABLE IF NOT EXISTS event_register (EVENT_ID INT PRIMARY KEY, EVENT_KEY INT NOT NULL, EVENT VARCHAR(255) NOT NULL, event_time BIGINT NOT NULL);


INSERT INTO event_register (EVENT_ID, EVENT_KEY, EVENT, event_time) VALUES (1, 1,'event1', 9L); -- return this if request has timestamp = 9
INSERT INTO event_register (EVENT_ID, EVENT_KEY, EVENT, event_time) VALUES (2, 2, 'event2', 9L);
INSERT INTO event_register (EVENT_ID, EVENT_KEY, EVENT, event_time) VALUES (3, 1, 'event1.1', 10L); -- return this if request has timestamp = 10 or 11
INSERT INTO event_register (EVENT_ID, EVENT_KEY, EVENT, event_time) VALUES (4, 1, 'event1.2', 12L); -- return this if request has timestamp = 12 or 13