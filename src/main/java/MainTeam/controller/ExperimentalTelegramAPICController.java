package MainTeam.controller;

import MainTeam.entity.ChatDependency;
import MainTeam.entity.Consultation;
import MainTeam.entity.ConsultationStateMachine;
import MainTeam.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/experimental")
public class ExperimentalTelegramAPICController {

    @Value("${token}")
    private String token;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    final LinkedHashMap<Long, ChatDependency> mapCacheChats = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > 1000;
        }
    };

    public ExperimentalTelegramAPICController(ObjectMapper objectMapper, RestTemplate restTemplate,
        HttpHeaders httpHeaders) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    private URI getSendURI() {
        try {
            return new URI("https://api.telegram.org/bot" + token + "/sendMessage");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String text, Long chatId, Map<String, String> additionalFields) {

        Map<String, String> map = new HashMap<>();
        map.put("chat_id", chatId.toString());
        map.put("text", text);

        if (additionalFields != null) {
            map.putAll(additionalFields);
        }

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, httpHeaders);
        restTemplate.postForEntity(getSendURI(), entity, String.class);
    }

    @Scheduled(fixedRate = 60 * 1000, initialDelay = 60 * 1000)
    private void scheduleFixedRateTask() {
        for (Map.Entry<Long, ChatDependency> entry : mapCacheChats.entrySet()) {

            if((System.currentTimeMillis() / 1000) - entry.getValue().getTimestamp() >= 20 * 60){
                mapCacheChats.remove(entry.getKey());
            }

        }
    }

    private void logic(Message message, ChatDependency chatDependency) throws JsonProcessingException {
        if (message.getText().equals("/start")) {

            mapCacheChats.put(message.getChatId(), chatDependency);
            Map<String, String> map = new HashMap<>();
            map.put("reply_markup",
                "{\"keyboard\": [ [{\"text\": \"Получить консультацию\"}], [{ \"text\": \"Заказать запчасть\"}] ]}");
            sendMessage("Что бы вы хотели?", message.getChatId(), map);

        } else if (message.getText().equalsIgnoreCase("получить консультацию")) {

            Map<String, String> map = new HashMap<>();
            map.put("reply_markup", "{\"remove_keyboard\": true}");

            sendMessage("Ваше имя:", message.getChatId(), map);
            chatDependency.setConsultationStateMachine(ConsultationStateMachine.NAME);

        } else if (chatDependency.getConsultationStateMachine() != ConsultationStateMachine.INITIATION) {
            switch (chatDependency.getConsultationStateMachine()) {
                case NAME -> {
                    System.out.println((Thread.currentThread().getName()));
                    chatDependency.getConsultation().setName(message.getText());
                    sendMessage("Модель:", message.getChatId(), null);
                    chatDependency.setConsultationStateMachine(ConsultationStateMachine.MODEL);
                }
                case MODEL -> {
                    chatDependency.getConsultation().setModel(message.getText());
                    sendMessage("Описание проблемы:", message.getChatId(), null);
                    chatDependency.setConsultationStateMachine(ConsultationStateMachine.DESCRIPTION);
                }
                case DESCRIPTION -> {
                    chatDependency.getConsultation().setDescription(message.getText());
                    sendMessage("Адрес:", message.getChatId(), null);
                    chatDependency.setConsultationStateMachine(ConsultationStateMachine.ADDRESS);
                }
                case ADDRESS -> {
                    chatDependency.getConsultation().setAddress(message.getText());
                    chatDependency.setConsultationStateMachine(ConsultationStateMachine.INITIATION);
                    sendMessage("Звонок заказан!", message.getChatId(), null);
                    sendMessage(objectMapper.writeValueAsString(chatDependency.getConsultation()), message.getChatId(),
                        null);
                    mapCacheChats.remove(message.getChatId());
                }
            }
        } else {
            sendMessage("Не знаю такой команды!", message.getChatId(), null);
        }
    }

    @PostMapping
    public void takeBody(@RequestBody String string) throws JsonProcessingException {
        Message message = objectMapper.readValue(string, Message.class);

        ChatDependency chatDependency;
        if (!mapCacheChats.containsKey(message.getChatId())) {
            chatDependency = new ChatDependency();
            chatDependency.setChatId(message.getChatId());
            chatDependency.setConsultation(new Consultation());
            chatDependency.setConsultationStateMachine(ConsultationStateMachine.INITIATION);
            chatDependency.setTimestamp(System.currentTimeMillis() / 1000);
        } else {
            chatDependency = mapCacheChats.get(message.getChatId());
        }

        new Thread(() -> {
            try {
                logic(message, chatDependency);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
