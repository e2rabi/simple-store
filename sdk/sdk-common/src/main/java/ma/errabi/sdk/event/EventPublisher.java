package ma.errabi.sdk.event;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final StreamBridge streamBridge;

    public <K, T> void publishEvent(Event<K, T> event) {
        streamBridge.send("output-out-0", event);
    }

    public <K, T> void publishReviewEvent(Event<K, T> event) {
        streamBridge.send("output-out-1", event);
    }

    public <K, T> void publishRecommendationEvent(Event<K, T> event) {
        streamBridge.send("output-out-2", event);
    }
}