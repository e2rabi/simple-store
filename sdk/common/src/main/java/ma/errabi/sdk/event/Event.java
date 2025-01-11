package ma.errabi.sdk.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class Event<K,T> {
    public enum Type {
        CREATE, DELETE
    }
    private Event.Type eventType;
    private K key;
    private T data;
    private ZonedDateTime createdDate ;

    public Event() {
        eventType = null;
        key = null;
        data = null;
        createdDate = null;
    }
    public Event(T data, K key, Type eventType) {
        this.data = data;
        this.key = key;
        this.eventType = eventType;
        this.createdDate = ZonedDateTime.now();
    }
}
