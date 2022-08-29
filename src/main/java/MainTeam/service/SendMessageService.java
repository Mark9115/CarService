package MainTeam.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SendMessageService {

    @Value("${token}")
    private String TOKEN;

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    private URI getSendURI() {
        try {
            return new URI("https://api.telegram.org/bot" + TOKEN + "/sendMessage");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String text, Long chatId, Map<String, String> additionalFields) {

        Map<String, String> map = new HashMap<>();
        map.put("chat_id", chatId.toString());
        map.put("text", text);

        if (additionalFields != null) {
            map.putAll(additionalFields);
        }

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, httpHeaders);
        restTemplate.postForEntity(getSendURI(), entity, String.class);
    }
}
