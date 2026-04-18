package de.beispiel.versicherung;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "enable.chat-detection-test", havingValue = "true")
public class ChatDetectionStarter {
}
